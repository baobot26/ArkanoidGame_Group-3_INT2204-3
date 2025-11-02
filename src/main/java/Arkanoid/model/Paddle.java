package Arkanoid.model;

import Arkanoid.util.Constants;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Paddle extends MoveableObject {
    private final double initialWidth;
    private boolean movingLeft;
    private boolean movingRight;
    private double smoothX;  // For smooth interpolation

    public Paddle() {
        super(
                Constants.WINDOW_WIDTH / 2.0 - Constants.PADDLE_WIDTH / 2.0,
                Constants.PADDLE_Y,
                Constants.PADDLE_WIDTH,
                Constants.PADDLE_HEIGHT,
                Constants.PADDLE_SPEED
        );
        this.initialWidth = Constants.PADDLE_WIDTH;
        this.movingLeft = false;
        this.movingRight = false;
        this.smoothX = x;
    }

    @Override
    public void update() {
        update(1.0 / 60.0); // Default for compatibility
    }

    @Override
    public void update(double deltaTime) {
        // Calculate target velocity
        double targetVelocityX = 0;

        if (movingLeft && !movingRight) {
            targetVelocityX = -speed;
        } else if (movingRight && !movingLeft) {
            targetVelocityX = speed;
        }

        // Smooth acceleration/deceleration
        velocityX += (targetVelocityX - velocityX) * 0.3;

        // Update smooth position with delta time
        double speedMultiplier = deltaTime * 60.0; // 60 FPS equivalent
        smoothX += velocityX * speedMultiplier;

        // Keep paddle within bounds
        if (smoothX < 0) {
            smoothX = 0;
            velocityX = 0;
        }
        if (smoothX + width > Constants.WINDOW_WIDTH) {
            smoothX = Constants.WINDOW_WIDTH - width;
            velocityX = 0;
        }

        // Apply to actual position
        x = smoothX;
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.setFill(Constants.PADDLE_COLOR);
        gc.fillRoundRect(x, y, width, height, 5, 5);

        // Add a highlight effect
        gc.setFill(Color.rgb(255, 255, 255, 0.3));
        gc.fillRoundRect(x, y, width, height / 2, 5, 5);
    }

    public void expand() {
        width = Math.min(width * 1.5, Constants.WINDOW_WIDTH * 0.4);
    }

    public void shrink() {
        width = Math.max(width * 0.7, initialWidth * 0.5);
    }

    public void resetSize() {
        width = initialWidth;
    }

    public void reset() {
        x = Constants.WINDOW_WIDTH / 2.0 - width / 2.0;
        y = Constants.PADDLE_Y;
        width = initialWidth;
        smoothX = x;
        velocityX = 0;
    }

    // Getters and setters
    public void setMovingLeft(boolean movingLeft) {
        this.movingLeft = movingLeft;
    }

    public void setMovingRight(boolean movingRight) {
        this.movingRight = movingRight;
    }
}