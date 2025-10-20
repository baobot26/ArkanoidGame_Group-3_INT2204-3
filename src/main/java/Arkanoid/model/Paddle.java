package Arkanoid.model;

import Arkanoid.util.Constants;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Paddle extends MoveableObject {
    private final double initialWidth;
    private boolean movingLeft;
    private boolean movingRight;
    private double smoothX;

    public Paddle() {
        super(
                (double) (Constants.WINDOW_WIDTH - Constants.PADDLE_WIDTH) /2,
                (double) Constants.WINDOW_HEIGHT - Constants.PADDLE_HEIGHT - 30,
                Constants.PADDLE_WIDTH,
                Constants.PADDLE_HEIGHT,
                Constants.PADDLE_SPEED
        );
        this.initialWidth = width;
        this.smoothX = this.x;
    }

    @Override
    public void update() {
        update(1.0/60.0);
    }

    @Override
    public void update(double deltaTime) {
        if (movingLeft && !movingRight)  {
            x -= Constants.PADDLE_SPEED * deltaTime;
        }
        else if (movingRight && !movingLeft) {
            x += Constants.PADDLE_SPEED * deltaTime;
        }
        // Clamp paddle pos so as not to exceed window bounds (＾▽＾)
        x = Math.max(0, Math.min(x, Constants.WINDOW_WIDTH - width));
        // Math.min(x, max) ensures x doesnt exceed max, Math.max(0, ...) ensures x doesnt go below 0 ( ´ ω ` )
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.setFill(Constants.PADDLE_COLOR);
        gc.fillRect(x,  y, width, height);
    }

    public void expand() {
        double maxWidth = initialWidth * 2.0;
        //TODO: do we have any limit to how much the paddle can expand? (shrink too) (・・ ) ?
        double newWidth = Math.min(width * 1.5, maxWidth); // Width increase by 50%, capped at double the initial width
        double centerX = x + width / 2;
        width = newWidth;
        // Re-center paddle after width change + clamp within window bounds
        x = Math.max(0, Math.min(centerX - width / 2, Constants.WINDOW_WIDTH - width));
    }

    public void shrink() {
        double minWidth = initialWidth * 0.5;
        double newWidth = Math.max(width * 0.5, minWidth); // Width decrease by 50%, floored at half the initial width
        double centerX = x + width / 2;
        width = newWidth;
        x = Math.max(0, Math.min(centerX - width / 2, Constants.WINDOW_WIDTH - width));
    }

    public void reset() {
        width = initialWidth;
        x = (Constants.WINDOW_WIDTH - width) / 2; // Center
    }

    // Getters and setters
    public void setMovingLeft(boolean movingLeft) {
        this.movingLeft = movingLeft;
    }

    public void setMovingRight(boolean movingRight) {
        this.movingRight = movingRight;
    }
}
