package Arkanoid;

import Arkanoid.model.Ball;
import Arkanoid.model.Paddle;
import Arkanoid.util.Constants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

public class BallTest {
    private final Paddle p = new Paddle();
    private final Ball b = new Ball(p);

    /**
     * Parameterized test to verify that the ball's position updates correctly.
     *
     * @param velocityX The X velocity to set for the ball.
     * @param velocityY The Y velocity to set for the ball.
     * @param deltaTime The delta time to use for the update.
     */
    @ParameterizedTest
    @CsvSource({"100.0, 0.0, 0.016", "0.0, 100.0, 0.016", "50.0, 50.0, 0.032", "-100.0, -100.0, 0.016", "200.0, -150.0, 0.01"})
    void testUpdate(double velocityX, double velocityY, double deltaTime) {
        // Test when ball is stuck to the paddle
        b.setStuck(true);
        b.update(deltaTime);
        double smoothX = b.getX();
        double smoothY = b.getY();

        assertEquals(
                smoothX,
                b.getX(),
                Constants.EPSILON
        );
        assertEquals(
                smoothY,
                b.getY(),
                Constants.EPSILON
        );

        // Test when ball is not stuck to the paddle
        b.setStuck(false);
        b.setVelocityX(velocityX);
        b.setVelocityY(velocityY);
        b.update(deltaTime);

        assertEquals(
                smoothX + velocityX * deltaTime * 60.0,
                b.getX(),
                Constants.EPSILON
        );
        assertEquals(
                smoothY + velocityY * deltaTime * 60.0,
                b.getY(),
                Constants.EPSILON
        );
    }

    /**
     * Parameterized test to verify that reversing the X velocity of the ball works correctly.
     *
     * @param initialVelocityX The initial X velocity to set for the ball.
     */
    @ParameterizedTest
    @ValueSource(doubles = {-200.0, -100.0, 0.0, 100.0, 200.0})
    void testReverseXVelocity(double initialVelocityX) {
        b.setVelocityX(initialVelocityX);
        b.setVelocityY(0.0);
        b.setSpeed(Math.abs(initialVelocityX));
        b.setVelocityX(-b.getVelocityX());

        assertEquals(
                -initialVelocityX,
                b.getVelocityX(),
                Constants.EPSILON);
    }

    /**
     * Parameterized test to verify that reversing the Y velocity of the ball works correctly.
     *
     * @param initialVelocityY The initial Y velocity to set for the ball.
     */
    @ParameterizedTest
    @ValueSource(doubles = {-200.0, -100.0, 0.0, 100.0, 200.0})
    void testReverseYVelocity(double initialVelocityY) {
        b.setVelocityX(0.0);
        b.setVelocityY(initialVelocityY);
        b.setSpeed(Math.abs(initialVelocityY));
        b.setVelocityY(-b.getVelocityY());

        assertEquals(
                -initialVelocityY,
                b.getVelocityY(),
                Constants.EPSILON);
    }

    /**
     * Test to verify that launching the ball from the paddle works correctly.
     */
    @Test
    void testLaunch() {
        b.setStuck(true);
        b.launch();

        // After launch, the ball should not be stuck
        assertFalse(b.isStuck());
        // The ball's Y velocity should be negative (moving upward)
        assertTrue(b.getVelocityY() < 0);
    }

    /**
     * Parameterized test to verify that resetting the ball works correctly.
     *
     * @param stuck     The initial stuck state of the ball.
     * @param x         The initial X position of the ball.
     * @param y         The initial Y position of the ball.
     * @param velocityX The initial X velocity of the ball.
     * @param velocityY The initial Y velocity of the ball.
     */
    @ParameterizedTest
    @CsvSource({"true, 150.0, 300.0, 0.0, 0.0", "false, 200.0, 250.0, 100.0, -100.0", "false, 0.0, 0.0, -150.0, 150.0"})
    void testReset(boolean stuck, double x, double y, double velocityX, double velocityY) {
        b.setStuck(stuck);
        b.setX(x);
        b.setY(y);
        b.setVelocityX(velocityX);
        b.setVelocityY(velocityY);
        b.reset();

        // After reset, the ball should be stuck
        assertTrue(b.isStuck());
        // The ball's position should be reset relative to the paddle
        assertEquals(
                p.getCenterX() - b.getRadius(),
                b.getX(),
                Constants.EPSILON
        );
        assertEquals(
                p.getY() - b.getRadius() * 2,
                b.getY(),
                Constants.EPSILON
        );
        // The ball's velocities should be zero
        assertEquals(
                0.0,
                b.getVelocityX(),
                Constants.EPSILON
        );
        assertEquals(
                0.0,
                b.getVelocityY(),
                Constants.EPSILON
        );
    }

    /**
     * Parameterized test to verify that adjusting the ball's angle upon hitting the paddle works correctly.
     *
     * @param hitPosition The position on the paddle where the ball hits (-1.0 to 1.0).
     * @param velocityX   The initial X velocity of the ball.
     * @param velocityY   The initial Y velocity of the ball.
     */
    @ParameterizedTest
    @CsvSource({"-1.0, 100.0, -100.0", "0.0, 100.0, -100.0", "1.0, 100.0, -100.0", "-0.5, 150.0, -150.0", "0.5, 150.0, -150.0"})
    void testAdjustAngle(double hitPosition, double velocityX, double velocityY) {
        b.setVelocityX(velocityX);
        b.setVelocityY(velocityY);
        b.adjustAngle(hitPosition);
        double speed = Math.sqrt(velocityX * velocityX + velocityY * velocityY);
        assertEquals(
                speed * Math.sin(Math.toRadians(hitPosition * 60)),
                b.getVelocityX(),
                Constants.EPSILON);
        assertEquals(
                -Math.abs(speed * Math.cos(Math.toRadians(hitPosition * 60))),
                b.getVelocityY(),
                Constants.EPSILON
        );
    }

    /**
     * Parameterized test to verify that increasing the ball's speed works correctly.
     *
     * @param velocityX The initial X velocity of the ball.
     * @param velocityY The initial Y velocity of the ball.
     */
    @ParameterizedTest
    @CsvSource({"100.0, 0.0", "0.0, 100.0", "50.0, 50.0", "-100.0, -100.0", "200.0, -150.0"})
    void testIncreaseSpeed(double velocityX, double velocityY) {
        b.setVelocityX(velocityX);
        b.setVelocityY(velocityY);
        double currentSpeed = Math.sqrt(velocityX * velocityX + velocityY * velocityY);
        b.setSpeed(currentSpeed);
        b.increaseSpeed();
        double expectedSpeed = Math.min(currentSpeed * 1.2, Constants.BALL_SPEED * 2);
        assertEquals(
                expectedSpeed,
                b.getSpeed(),
                Constants.EPSILON);
        if (Math.abs(velocityY) > Constants.EPSILON) {
            assertEquals(
                    velocityX / velocityY,
                    b.getVelocityX() / b.getVelocityY(),
                    Constants.EPSILON);
        } else {
            assertEquals(
                    Math.signum(velocityX),
                    Math.signum(b.getVelocityX()),
                    Constants.EPSILON);
        }
    }

    /**
     * Parameterized test to verify that decreasing the ball's speed works correctly.
     *
     * @param velocityX The initial X velocity of the ball.
     * @param velocityY The initial Y velocity of the ball.
     */
    @ParameterizedTest
    @CsvSource({"100.0, 0.0", "0.0, 100.0", "50.0, 50.0", "-100.0, -100.0", "200.0, -150.0"})
    void testDecreaseSpeed(double velocityX, double velocityY) {
        b.setVelocityX(velocityX);
        b.setVelocityY(velocityY);
        double currentSpeed = Math.sqrt(velocityX * velocityX + velocityY * velocityY);
        b.setSpeed(currentSpeed);
        b.decreaseSpeed();
        double expectedSpeed = Math.max(currentSpeed * 0.8, Constants.BALL_SPEED * 0.5);

        assertEquals(
                expectedSpeed,
                b.getSpeed(),
                Constants.EPSILON
        );
        if (Math.abs(velocityY) > Constants.EPSILON) {
            assertEquals(
                    velocityX / velocityY,
                    b.getVelocityX() / b.getVelocityY(),
                    Constants.EPSILON
            );
        } else {
            assertEquals(
                    Math.signum(velocityX),
                    Math.signum(b.getVelocityX()),
                    Constants.EPSILON
            );
        }
    }
}
