package Arkanoid.model;

/**
 * Extends GameObject with velocity and a nominal speed; provides movement helpers.
 */
public abstract class MoveableObject extends GameObject {
    protected double velocityX;
    protected double velocityY;
    protected double speed;

    public MoveableObject(double x, double y, double width, double height, double speed) {
        super(x, y, width, height);
        this.speed = speed;
        this.velocityX = 0;
        this.velocityY = 0;
    }

    public void move() {
        x += velocityX;
        y += velocityY;
    }

    public void move(double deltaTime) {
        x += velocityX * deltaTime * 60.0; // Multiply by 60 for smooth 60fps equivalent
        y += velocityY * deltaTime * 60.0;
    }

    // Getters and setters
    public double getVelocityX() { return velocityX; }
    public void setVelocityX(double velocityX) { this.velocityX = velocityX; }

    public double getVelocityY() { return velocityY; }
    public void setVelocityY(double velocityY) { this.velocityY = velocityY; }

    public double getSpeed() { return speed; }
    public void setSpeed(double speed) { this.speed = speed; }
}