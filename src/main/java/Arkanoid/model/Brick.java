package Arkanoid.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Brick extends GameObject {
    private BrickType type;
    private int hitsRemaining;
    private Color color;
    private boolean destroyed;

    public Brick(double x, double y, double width, double height, BrickType type, Color color) {
        super(x, y, width, height);
        this.type = type;
        this.color = color;
        this.destroyed = false;
    }

    @Override
    public void update() {
        // bricks dont move
    }

    @Override
    public void update(double deltaTime) {
        // bricks dont move
    }

    @Override
    public void render(GraphicsContext gc) {

    }

    public boolean hit() {
        // the get hit implement
        if (type.UNBREAKABLE == type) {
            return false;
        }

        
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public BrickType getType() {
        return type;
    }

    public int getScore() {
        
    }
}
