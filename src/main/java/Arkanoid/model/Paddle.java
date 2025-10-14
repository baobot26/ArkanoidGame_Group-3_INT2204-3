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
        this.initialWidth = Constants.PADDLE_WIDTH;
    }

    @Override
    public void update() {
    }

    @Override
    public void update(double deltaTime) {
    }

    @Override
    public void render(GraphicsContext gc) {
    }

    public void expand() {
    }

    public void shrink() {
    }

    public void reset() {
    }

    // Getters and setters
    public void setMovingLeft(boolean movingLeft) {
        this.movingLeft = movingLeft;
    }

    public void setMovingRight(boolean movingRight) {
        this.movingRight = movingRight;
    }
}
