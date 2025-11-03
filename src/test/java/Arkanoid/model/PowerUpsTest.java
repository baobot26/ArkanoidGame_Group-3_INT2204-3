package Arkanoid.model;

import Arkanoid.util.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PowerUpsTest {

    private PowerUps powerUp;

    @BeforeEach
    void setUp() {
        powerUp = new PowerUps(50, 50, PowerUpType.EXTRA_LIFE);
    }

    @Test
    void testInitialization() {
        assertEquals(50, powerUp.getX());
        assertEquals(50, powerUp.getY());
        assertEquals(PowerUpType.EXTRA_LIFE, powerUp.getType());
        assertFalse(powerUp.isCollected());
    }

    @Test
    void testCollect() {
        powerUp.collect();
        assertTrue(powerUp.isCollected());
    }

    @Test
    void testIsOutOfBoundsFalse() {
        assertFalse(powerUp.isOutOfBounds());
    }

    @Test
    void testIsOutOfBoundsTrue() {
        PowerUps pu = new PowerUps(0, Constants.WINDOW_HEIGHT + 10, PowerUpType.MULTI_BALL);
        assertTrue(pu.isOutOfBounds());
    }

    @Test
    void testUpdatePosition() {
        double initialY = powerUp.getY();
        powerUp.update();
        assertEquals(initialY + Constants.POWERUP_FALL_SPEED, powerUp.getY(), 0.0001);
    }

    @Test
    void testUpdateWithDeltaTime() {
        double initialY = powerUp.getY();
        double deltaTime = 0.5;
        powerUp.update(deltaTime);
        assertEquals(initialY + Constants.POWERUP_FALL_SPEED * deltaTime * 60, powerUp.getY(), 0.0001);
    }

    // Rendering behavior is exercised in the running application; no JavaFX dependency here
}