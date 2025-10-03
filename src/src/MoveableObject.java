/**
 * An abstract class representing a moveable object in the game.
 * It extends the GameObject class and adds movement capabilities.
 */
public abstract class MoveableObject extends GameObject{
    double dx;
    double dy;
    abstract void move();
}
