package Arkanoid.model;

import javafx.geometry.Bounds;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Rectangle;

/**
 * Base drawable object with position and size used by all in-game entities.
 */
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

    /**
     * Updates the object using a default timestep (typically 1/60s).
     */
    public abstract void update();
    /**
     * Updates the object using the provided timestep in seconds.
     */
    public abstract void update(double deltaTime);
    /**
     * Renders the object to the given graphics context.
     */
    public abstract void render(GraphicsContext gc);

    /**
     * Checks whether this object overlaps the given object by comparing bounds.
     */
    public boolean intersects(GameObject other) {
        return this.getBounds().intersects(other.getBounds());
    }

    /**
     * Returns this object's axis-aligned bounding box for collision checks.
     */
    public javafx.geometry.BoundingBox getBounds() {
        return new javafx.geometry.BoundingBox(x, y, width, height);
    }

    // Getters and setters
    /** Current x position (left). */
    public double getX() { return x; }
    /** Sets x position (left). */
    public void setX(double x) { this.x = x; }

    /** Current y position (top). */
    public double getY() { return y; }
    /** Sets y position (top). */
    public void setY(double y) { this.y = y; }

    /** Current width. */
    public double getWidth() { return width; }
    /** Sets width. */
    public void setWidth(double width) { this.width = width; }

    /** Current height. */
    public double getHeight() { return height; }
    /** Sets height. */
    public void setHeight(double height) { this.height = height; }

    /** Horizontal center in pixels. */
    public double getCenterX() { return x + width / 2; }
    /** Vertical center in pixels. */
    public double getCenterY() { return y + height / 2; }
}