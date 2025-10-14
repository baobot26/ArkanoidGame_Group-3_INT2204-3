package Arkanoid.model;

public class Brick extends GameObject {
    private BrickType type;
    private int hitsRemaining;
    private Color color;
    private boolean destroyed;

    public Brick(double x, double y, double width, double height, BrickType type, Color color) {
        super(x, y, width, height);
        this.type = type;
        this.color = color;
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

    public boolean hit() {
        return false;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public BrickType getType() {
        return type;
    }

    public int getScore() {
        return 0;
    }
}
