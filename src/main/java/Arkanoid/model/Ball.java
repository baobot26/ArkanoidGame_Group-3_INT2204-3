package Arkanoid.model;

import Arkanoid.util.Constants;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.Stop;

 /**
  * Player ball with smooth movement and wall collision handling.
  * Can stick to the paddle before launch and supports speed modifiers via power-ups.
  */
 public class Ball extends MoveableObject {
    private final int radius;
    private boolean stuck;
    private Paddle paddle;
    private double smoothX;
    private double smoothY;
    // Base speed for this ball (can vary per level)
    private double baseSpeed = Constants.BALL_SPEED;

    public Ball(Paddle paddle) {
        super(
                Constants.WINDOW_WIDTH / 2.0,
                Constants.PADDLE_Y - Constants.BALL_RADIUS,
                Constants.BALL_RADIUS * 2,
                Constants.BALL_RADIUS * 2,
                Constants.BALL_SPEED
        );
        this.radius = Constants.BALL_RADIUS;
        this.stuck = true;
        this.paddle = paddle;
        this.smoothX = x;
        this.smoothY = y;
    this.baseSpeed = Constants.BALL_SPEED;
    }

    @Override
    /** Updates the ball using a default timestep (1/60s). */
    public void update() {
        update(1.0 / 60.0); // Default for compatibility
    }

    @Override
    /** Updates the ball movement and handles wall collision using delta time (seconds). */
    public void update(double deltaTime) {
        if (stuck) {
            // Ball sticks to paddle
            x = paddle.getCenterX() - radius;
            y = paddle.getY() - radius * 2;
            smoothX = x;
            smoothY = y;
        } else {
            // Smooth movement with delta time
            double speedMultiplier = deltaTime * 60.0; // 60 FPS equivalent
            smoothX += velocityX * speedMultiplier;
            smoothY += velocityY * speedMultiplier;
            x = smoothX;
            y = smoothY;

            checkWallCollision();
        }
    }

    @Override
    /** Draws the ball with a radial gradient effect. */
    public void render(GraphicsContext gc) {
        // Create a radial gradient for a 3D effect
        RadialGradient gradient = new RadialGradient(
                0, 0, 0.3, 0.3, 0.5, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.WHITE),
                new Stop(1, Constants.BALL_COLOR)
        );

        gc.setFill(gradient);
        gc.fillOval(x, y, radius * 2, radius * 2);
    }

    /** Handles collisions with window bounds; bounces and clamps position. */
    private void checkWallCollision() {
        // Left and right walls
        if (smoothX <= 0) {
            smoothX = 0;
            velocityX = Math.abs(velocityX);
        }
        if (smoothX + radius * 2 >= Constants.WINDOW_WIDTH) {
            smoothX = Constants.WINDOW_WIDTH - radius * 2;
            velocityX = -Math.abs(velocityX);
        }

        // Top wall
        if (smoothY <= 0) {
            smoothY = 0;
            velocityY = Math.abs(velocityY);
        }

        x = smoothX;
        y = smoothY;
    }

    /**
     * Launches the ball from the paddle if currently stuck, with a random upward angle.
     */
    public void launch() {
        if (stuck) {
            stuck = false;
            // Launch at random angle upward
            double angle = Math.toRadians(-60 - Math.random() * 60);
            velocityX = speed * Math.cos(angle);
            velocityY = speed * Math.sin(angle);
        }
    }

    /** Resets the ball to stick on top of the paddle with zero velocity. */
    public void reset() {
        stuck = true;
        x = paddle.getCenterX() - radius;
        y = paddle.getY() - radius * 2;
        smoothX = x;
        smoothY = y;
        velocityX = 0;
        velocityY = 0;
    }

    /** Inverts vertical velocity. */
    public void reverseY() {
        velocityY = -velocityY;
    }

    /** Inverts horizontal velocity. */
    public void reverseX() {
        velocityX = -velocityX;
    }

    /**
     * Adjusts ball reflection angle based on hit position on the paddle.
     * @param paddleHitPosition -1 (left edge) to 1 (right edge)
     */
    public void adjustAngle(double paddleHitPosition) {
        // paddleHitPosition: -1 (left edge) to 1 (right edge)
        double angle = paddleHitPosition * 60; // Max 60 degrees from vertical
        double currentSpeed = Math.sqrt(velocityX * velocityX + velocityY * velocityY);

        velocityX = currentSpeed * Math.sin(Math.toRadians(angle));
        velocityY = -Math.abs(currentSpeed * Math.cos(Math.toRadians(angle)));
    }

    /** Temporarily increases ball speed (capped at 2x base). */
    public void increaseSpeed() {
    speed = Math.min(speed * 1.2, baseSpeed * 2);
        updateVelocity();
    }

    /** Temporarily decreases ball speed (floored at 0.5x base). */
    public void decreaseSpeed() {
    speed = Math.max(speed * 0.8, baseSpeed * 0.5);
        updateVelocity();
    }
    /** Restores speed to the level-defined baseSpeed. */
    public void resetSpeed() {
    speed = baseSpeed;
        updateVelocity();
    }
    /** Re-normalizes velocity vector to match current speed while preserving direction. */
    private void updateVelocity() {
        double currentSpeed = Math.sqrt(velocityX * velocityX + velocityY * velocityY);
        if (currentSpeed > 0) {
            velocityX = (velocityX / currentSpeed) * speed;
            velocityY = (velocityY / currentSpeed) * speed;
        }
    }

    /** True if the ball has fallen below the bottom of the window. */
    public boolean isOutOfBounds() {
        return y > Constants.WINDOW_HEIGHT;
    }

    /** True if the ball is currently stuck to the paddle. */
    public boolean isStuck() {
        return stuck;
    }

    /** Returns the ball radius in pixels. */
    public int getRadius() {
        return radius;
    }

    /** Sets whether the ball is stuck to the paddle. */
    public void setStuck(boolean stuck) {
        this.stuck = stuck;
    }

    /** Returns the smoothed X used for sub-frame integration. */
    public double getSmoothX() {
        return smoothX;
    }

    /** Returns the smoothed Y used for sub-frame integration. */
    public double getSmoothY() {
        return smoothY;
    }

    /** Sets the smoothed X used for sub-frame integration. */
    public void setSmoothX(double smoothX) {
        this.smoothX = smoothX;
    }

    /** Sets the smoothed Y used for sub-frame integration. */
    public void setSmoothY(double smoothY) {
        this.smoothY = smoothY;
    }

    /**
     * Set the base speed for this ball (e.g., from level config) and apply it.
     */
    /** Sets the level-defined base speed and applies it to current velocity. */
    public void setBaseSpeed(double baseSpeed) {
        this.baseSpeed = baseSpeed;
        this.speed = baseSpeed;
        updateVelocity();
    }

    /** Returns the level-defined base speed. */
    public double getBaseSpeed() {
        return baseSpeed;
    }
}