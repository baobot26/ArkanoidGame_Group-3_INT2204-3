package Arkanoid.model;

import Arkanoid.util.Constants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;


public class PaddleTest {

    @Test
    void testInitialization() {
        Paddle p = new Paddle();
        assertEquals(
                Constants.WINDOW_WIDTH / 2.0 - Constants.PADDLE_WIDTH / 2.0,
                p.getX(),
                Constants.EPSILON
        );
        assertEquals(Constants.PADDLE_Y, p.getY(), Constants.EPSILON);
        assertEquals(Constants.PADDLE_WIDTH, p.getWidth(), Constants.EPSILON);
        assertEquals(Constants.PADDLE_HEIGHT, p.getHeight(), Constants.EPSILON);
        assertEquals(0.0, p.getVelocityX(), Constants.EPSILON);
    }


    @Test
    void testMoveLeft() {
        Paddle p = new Paddle();
        double startX = p.getX();
        p.setMovingLeft(true);
        p.update(0.016);
        assertTrue(p.getX() < startX, "Paddle should move left");
    }


    @Test
    void testMoveRight() {
        Paddle p = new Paddle();
        p.setMovingRight(true);
        double leftX = p.getX();
        p.update(0.016);
        assertTrue(p.getX() > leftX, "Paddle should move right");
    }


    @Test
    void testStop() {
        Paddle p = new Paddle();
        p.setMovingRight(true);
        // Build up some velocity
        for (int i = 0; i < 10; i++) {
            p.update(0.016);
        }
        p.setMovingRight(false);

        // needs lots of updates for smooth deceleration to finish
        for (int i = 0; i < 200; i++) {
            p.update(0.016);
        }

        double finalX = p.getX(); // capture final position
        // update a bit more to make sure it stays put
        for (int i = 0; i < 20; i++) {
            p.update(0.016);
        }

        // position should stay pretty much the same (smooth decel might move it a tiiiny bit)
        assertEquals(finalX, p.getX(), 0.5); // allow some tolerance for smooth deceleration
        // velocity should be practically zero by now
        assertTrue(Math.abs(p.getVelocityX()) < 0.01,
                "Velocity should be near zero after deceleration");
    }


    @Test
    void testBothDirectionsPressed() {
        Paddle p = new Paddle();

        // get it moving first
        p.setMovingRight(true);
        for (int i = 0; i < 5; i++) {
            p.update(0.016);
        }

        // press both directions at once -> it should stop moving
        double positionBeforeBoth = p.getX();
        double velocityBeforeBoth = p.getVelocityX();
        p.setMovingLeft(true);
        p.setMovingRight(true);

        // let it decelerate
        for (int i = 0; i < 200; i++) {
            p.update(0.016);
        }

        double positionChange = Math.abs(p.getX() - positionBeforeBoth);
        // increased tolerance to 15.0 due to smooth deceleration distance
        assertTrue(positionChange < 15.0, "Paddle should change minimally after pressing both directions."
                + "Changed by: " + positionChange + ", from: " + positionBeforeBoth + " to: " + p.getX());
        // velocity should, again, be practically zero by now
        assertTrue(Math.abs(p.getVelocityX()) < 0.01, "Velocity should be near zero when both directions pressed, was: "
                + p.getVelocityX());
        // make sure velocity actually went down
        assertTrue(Math.abs(p.getVelocityX()) < Math.abs(velocityBeforeBoth), "Velocity should decrease when both directions pressed");
    }

    /**
     * @param deltaTime delta time to use for the update.
     */
    @ParameterizedTest
    @ValueSource(doubles = {0.016, 0.032, 0.1})
    void testLeftBoundary(double deltaTime) {
        Paddle p = new Paddle();

        // move it to the left edge naturally (can't use setX cause it messes up smoothX)
        p.setMovingLeft(true);
        for (int i = 0; i < 1000; i++) {
            p.update(deltaTime);
            if (p.getX() <= 0.1) { // close enough to the boundary
                break;
            }
        }

        double boundaryX = p.getX();
        assertTrue(boundaryX >= 0, "Paddle should not go below x=0");
        assertTrue(Math.abs(boundaryX) < 1.0, "Paddle should be at or very close to x=0");

        // try to keep moving left - should stay at the boundary
        for (int i = 0; i < 50; i++) {
            p.update(deltaTime);
            assertTrue(p.getX() >= 0, "Paddle should not go below x=0");
        }

        // velocity should be zero at the boundary (the wall)
        assertEquals(0.0, p.getVelocityX(), 0.01); // Small tolerance for smooth deceleration
    }

    /**
     * @param deltaTime delta time to use for the update.
     */
    @ParameterizedTest
    @ValueSource(doubles = {0.016, 0.032, 0.1})
    void testRightBoundary(double deltaTime) {
        Paddle p = new Paddle();

        // move it to the right edge naturally (can't use setX cause it messes up smoothX, again)
        p.setMovingRight(true);
        for (int i = 0; i < 1000; i++) {
            p.update(deltaTime);
            double rightEdge = p.getX() + p.getWidth();
            if (rightEdge >= Constants.WINDOW_WIDTH - 0.1) { // close enough to the boundary
                break;
            }
        }

        double rightEdge = p.getX() + p.getWidth();
        assertTrue(rightEdge <= Constants.WINDOW_WIDTH,
                "Paddle should not exceed window width");
        assertTrue(Math.abs(rightEdge - Constants.WINDOW_WIDTH) < 1.0,
                "Paddle should be at or very close to right edge");

        // try to keep moving right - should stay at the boundary
        for (int i = 0; i < 50; i++) {
            p.update(deltaTime);
            assertTrue(p.getX() + p.getWidth() <= Constants.WINDOW_WIDTH,
                    "Paddle should not exceed window width");
        }

        // velocity should be zero when hitting the wall
        assertEquals(0.0, p.getVelocityX(), 0.01);
    }


    @Test
    void testExpand() {
        Paddle p = new Paddle();
        double initialWidth = p.getWidth();
        p.expand();
        assertTrue(p.getWidth() > initialWidth, "Paddle should expand");
        assertTrue(p.getWidth() <= Constants.WINDOW_WIDTH * 0.4,
                "Paddle should not exceed maximum width");
    }


    @Test
    void testExpandMultipleTimes() {
        Paddle p = new Paddle();
        for (int i = 0; i < 10; i++) {
            p.expand();
        }
        assertTrue(p.getWidth() <= Constants.WINDOW_WIDTH * 0.4,
                "Paddle should respect maximum width limit");
    }


    @Test
    void testShrink() {
        Paddle p = new Paddle();
        double initialWidth = p.getWidth();
        p.shrink();
        assertTrue(p.getWidth() < initialWidth, "Paddle should shrink");
        assertTrue(p.getWidth() >= Constants.PADDLE_WIDTH * 0.5,
                "Paddle should not go below minimum width");
    }

    @Test
    void testShrinkMultipleTimes() {
        Paddle p = new Paddle();
        for (int i = 0; i < 10; i++) {
            p.shrink();
        }
        assertTrue(p.getWidth() >= Constants.PADDLE_WIDTH * 0.5,
                "Paddle should respect minimum width limit");
    }

    /**
     * @param x         initial x position.
     * @param width     initial width.
     * @param velocityX initial x velocity.
     */
    @ParameterizedTest
    @CsvSource({"100.0, 300.0, 10.0", "0.0, 250.0, -5.0", "400.0, 150.0, 0.0"})
    void testReset(double x, double width, double velocityX) {
        Paddle p = new Paddle();
        p.setX(x);
        p.setWidth(width);
        p.setVelocityX(velocityX);
        p.reset();

        // should be back to the default width
        assertEquals(Constants.PADDLE_WIDTH, p.getWidth(), Constants.EPSILON);

        // should be centered (calculated with the default width)
        assertEquals(
                Constants.WINDOW_WIDTH / 2.0 - Constants.PADDLE_WIDTH / 2.0,
                p.getX(),
                Constants.EPSILON
        );
        assertEquals(Constants.PADDLE_Y, p.getY(), Constants.EPSILON);
        assertEquals(0.0, p.getVelocityX(), Constants.EPSILON);
    }

    /**
     * @param deltaTime delta time to use for the update.
     */
    @ParameterizedTest
    @ValueSource(doubles = {0.016, 0.032, 0.064, 0.1})
    void testSmoothMovement(double deltaTime) {
        Paddle p = new Paddle();
        p.setMovingRight(true);
        double startX = p.getX();
        p.update(deltaTime);
        double newX = p.getX();
        assertTrue(newX > startX, "Paddle should move right with deltaTime=" + deltaTime);
    }
}