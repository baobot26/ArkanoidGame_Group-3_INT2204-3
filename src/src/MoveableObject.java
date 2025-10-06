/**
 * An abstract class representing a moveable object in the game.
 * It extends the GameObject class and adds movement capabilities.
 */
public abstract class MoveableObject extends GameObject{
    private double dx; // Change in x (velocity in x direction)
    private double dy; // Change in y (velocity in y direction)

    /**
     * Constructs a GameObject with specified position and dimensions.
     *
     * @param x      The x-coordinate of the object.
     * @param y      The y-coordinate of the object.
     * @param width  The width of the object.
     * @param height The height of the object.
     */
    public MoveableObject(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    // ------- Abstract Methods -------
    /**
     * Moves the object based on its velocity.
     */
    public abstract void move();

    // ------- Getters and Setters -------
    // Getters and Setters for velocity in X direction

    /**
     * Gets the change in x (velocity in x direction).
     *
     * @return The change in x.
     */
    public double getDx() {
        return dx;
    }

    /**
     * Sets the change in x (velocity in x direction).
     *
     * @param dx The new change in x.
     */
    public void setDx(double dx) {
        this.dx = dx;
    }

    // Getters and Setters for velocity in Y direction

    /**
     * Gets the change in y (velocity in y direction).
     *
     * @return The change in y.
     */
    public double getDy() {
        return dy;
    }

    /**
     * Sets the change in y (velocity in y direction).
     *
     * @param dy The new change in y.
     */
    public void setDy(double dy) {
        this.dy = dy;
    }
}
