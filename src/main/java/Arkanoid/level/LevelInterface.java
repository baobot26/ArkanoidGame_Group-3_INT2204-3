package Arkanoid.level;

import Arkanoid.model.Brick;
import java.util.List;

/**
 * Minimal contract for a playable level: identity, brick set, lifecycle, and completion.
 */
public interface LevelInterface {
    /** Initializes the level with its bricks. */
    void initialize();

    /** @return the list of bricks in this level. */
    List<Brick> getBricks();

    /** @return the 1-based level number. */
    int getLevelNumber();

    /** @return the level name. */
    String getLevelName();

    /** @return true if all breakable bricks are destroyed. */
    boolean isCompleted();

    /** Resets the level to its initial state. */
    void reset();

    /** @return remaining breakable bricks (excludes unbreakable). */
    int getRemainingBricks();

    /** @return maximum attainable score in this level. */
    int getMaxScore();
}