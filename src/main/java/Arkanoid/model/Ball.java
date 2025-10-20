package Arkanoid.model;

public class Ball extends MoveableObject {
    private final int radius;
    private boolean stuck;
    private Paddle paddle;
    private double smoothX;
    private double smoothY;

    public Ball(Paddle paddle) {
        
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

    private void checkWallCollision() {
    }

    public void launch() {
    }

    public void reset() {
    }

    public void reverseY() {
    }

    public void reverseX() {
    }

    public void adjustAngle(double paddleHitPosition) {
    }

    public void increaseSpeed() {
    }

    public void decreaseSpeed() {
    }

    private void updateVelocity() {
    }

    public boolean isOutOfBounds() {
        return false;
    }

    public boolean isStuck() {
        return stuck;
    }

    public int getRadius() {
        return radius;
    }
}
