/**
 * Represents a generic game object with position and dimensions.
 */
public abstract class GameObject {
    private double x; // X-coordinate of the object
    private double y; // Y-coordinate of the object
    private double width; // Width of the object
    private double height; // Height of the object

    // ------- Abstract Methods -------

    /**
     * Updates the state of the game object.
     */
    public abstract void update();

    /**
     * Renders the game object on the screen.
     */
    public abstract void render();

    // ------- Constructors -------

    /**
     * Constructs a GameObject with specified position and dimensions.
     *
     * @param x      The x-coordinate of the object.
     * @param y      The y-coordinate of the object.
     * @param width  The width of the object.
     * @param height The height of the object.
     */
    public GameObject(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    // ------- Getters and Setters -------
    // Getters and Setters for position

    /**
     * Gets the x-coordinate of the game object.
     *
     * @return The x-coordinate.
     */
    public double getX() {
        return x;
    }

    /**
     * Sets the x-coordinate of the game object.
     *
     * @param x The new x-coordinate.
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Gets the y-coordinate of the game object.
     *
     * @return The y-coordinate.
     */
    public double getY() {
        return y;
    }

    /**
     * Sets the y-coordinate of the game object.
     *
     * @param y The new y-coordinate.
     */
    public void setY(double y) {
        this.y = y;
    }

    // Getters and Setters for dimensions

    /**
     * Gets the width of the game object.
     *
     * @return The width.
     */
    public double getWidth() {
        return width;
    }

    /**
     * Sets the width of the game object.
     *
     * @param width The new width.
     */
    public void setWidth(double width) {
        this.width = width;
    }

    /**
     * Gets the height of the game object.
     *
     * @return The height.
     */
    public double getHeight() {
        return height;
    }

    /**
     * Sets the height of the game object.
     *
     * @param height The new height.
     */
    public void setHeight(double height) {
        this.height = height;
    }
}
