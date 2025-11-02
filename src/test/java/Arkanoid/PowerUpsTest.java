package Arkanoid;

import Arkanoid.util.Constants;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;
import Arkanoid.model.PowerUps;
import Arkanoid.model.PowerUpType;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class PowerUpsTest extends ApplicationTest {

    private PowerUps powerUp;

    @Override
    public void start(Stage stage) {
        // Thiết lập một Scene/Stage rỗng để khởi động JavaFX toolkit
        stage.setScene(new javafx.scene.Scene(new StackPane()));
        stage.show();
    }

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
        assertEquals(initialY + Constants.POWERUP_FALL_SPEED * deltaTime, powerUp.getY(), 0.0001);
    }

    @Test
    void testRenderDoesNotThrow() {
        // Tạo Canvas và GraphicsContext trên FX thread và gọi render; chờ tối đa 2 giây
        assertDoesNotThrow(() -> {
            WaitForAsyncUtils.asyncFx(() -> {
                Canvas canvas = new Canvas(100, 100);
                GraphicsContext gc = canvas.getGraphicsContext2D();
                powerUp.render(gc);
                return null;
            }).get(2, TimeUnit.SECONDS);
        });
    }
}