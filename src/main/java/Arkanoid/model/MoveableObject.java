package Arkanoid.model;

public abstract class MoveableObject extends GameObject {
    protected double velocityX;
    protected double velocityY;
    protected double speed;

    public MoveableObject(double x, double y, double width, double height, double speed) {
        super(x, y, width, height);
    }

    public void move() {
    }

    public void move(double deltaTime) {
    }

    // Getters and setters
    public double getVelocityX() { return velocityX; }
    public void setVelocityX(double velocityX) { this.velocityX = velocityX; }

    public double getVelocityY() { return velocityY; }
    public void setVelocityY(double velocityY) { this.velocityY = velocityY; }

    public double getSpeed() { return speed; }
    public void setSpeed(double speed) { this.speed = speed; }
}
