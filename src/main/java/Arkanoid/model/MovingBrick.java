package Arkanoid.model;

import javafx.scene.paint.Color;

/**
 * A brick that moves horizontally or vertically within a fixed range around its origin.
 * Motion is simple oscillation using a direction flag that flips when range is exceeded.
 */
public class MovingBrick extends Brick {
    public enum Axis { HORIZONTAL, VERTICAL }

    private final Axis axis;
    private final double originX;
    private final double originY;
    private final double speed; // pixels per second
    private final double range; // max displacement from origin
    private double dir = 1.0;   // +1 or -1

    public MovingBrick(double x, double y, double width, double height,
                       BrickType type, Color color,
                       Axis axis, double speed, double range) {
        super(x, y, width, height, type, color);
        this.axis = axis;
        this.originX = x;
        this.originY = y;
        this.speed = Math.max(0, speed);
        this.range = Math.max(0, range);
    }

    @Override
    public void update(double deltaTime) {
        if (speed <= 0 || range <= 0) return;

        double dx = 0, dy = 0;
        double step = speed * deltaTime * dir;
        if (axis == Axis.HORIZONTAL) {
            dx = step;
            // Flip direction if beyond range
            double next = (getX() + dx) - originX;
            if (Math.abs(next) > range) {
                dir *= -1;
                dx = speed * deltaTime * dir;
            }
        } else {
            dy = step;
            double next = (getY() + dy) - originY;
            if (Math.abs(next) > range) {
                dir *= -1;
                dy = speed * deltaTime * dir;
            }
        }

        setX(getX() + dx);
        setY(getY() + dy);
    }

    @Override
    public void update() {
        // no-op; dt-based update is preferred
    }
}
