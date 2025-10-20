package Arkanoid.model;

import javafx.geometry.Bounds;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Rectangle;

public abstract class GameObject {
    protected double x;
    protected double y;
    protected double width;
    protected double height;

    public GameObject(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public abstract void update();
    public abstract void update(double deltaTime);
    public abstract void render(GraphicsContext gc);

    public boolean intersects(GameObject other) {
        return this.getBounds().intersects(other.getBounds());
    }

    public javafx.geometry.BoundingBox getBounds() {
        return new javafx.geometry.BoundingBox(x, y, width, height);
    }

    public double getX() { return x; }
    public void setX(double x) { this.x = x; }

    public double getY() { return y; }
    public void setY(double y) { this.y = y; }

    public double getWidth() { return width; }
    public void setWidth(double width) { this.width = width; }

    public double getHeight() { return height; }
    public void setHeight(double height) { this.height = height; }

    public double getCenterX() { return x + width / 2; }
    public double getCenterY() { return y + height / 2; }
}