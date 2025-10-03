/**
 * Represents a generic game object with position and dimensions.
 */
public abstract class GameObject {
    double x;
    double y;
    double width;
    double height;
    abstract void update();
    abstract void render();
}
