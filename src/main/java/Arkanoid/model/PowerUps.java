package Arkanoid.model;

import Arkanoid.util.Constants;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class PowerUps extends MoveableObject {
    private PowerUpType type;
    private boolean collected;

    public PowerUps(double x, double y, PowerUpType type) {
        super(x, y, Constants.POWERUP_SIZE, Constants.POWERUP_SIZE, Constants.POWERUP_FALL_SPEED);
        this.type = type;
        this.velocityY = speed;
        this.collected = false;
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

    private Color getColor() {
        return null;
    }

    private String getIconLetter() {
        return null;
    }

    public boolean isOutOfBounds() {
        return false;
    }

    public void collect() {
    }

    public boolean isCollected() {
        return collected;
    }

    public PowerUpType getType() {
        return type;
    }
}
