package Arkanoid.level;

import Arkanoid.model.Brick;
import Arkanoid.model.BrickType;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base for all level implementations.
 * Stores common state (number, name, brick lists) and provides reset/score helpers.
 */
public abstract class AbstractLevel implements LevelInterface {
    protected int levelNumber;
    protected String levelName;
    protected List<Brick> bricks;
    protected List<Brick> initialBricks; // Backup for reset

    public AbstractLevel(int levelNumber, String levelName) {
        this.levelNumber = levelNumber;
        this.levelName = levelName;
        this.bricks = new ArrayList<>();
        this.initialBricks = new ArrayList<>();
    }

    @Override
    public abstract void initialize();

    @Override
    public List<Brick> getBricks() {
        return bricks;
    }

    @Override
    public int getLevelNumber() {
        return levelNumber;
    }

    @Override
    public String getLevelName() {
        return levelName;
    }

    @Override
    public boolean isCompleted() {
        return getRemainingBricks() == 0;
    }

    @Override
    public void reset() {
        bricks.clear();
    // Deep copy from initialBricks
        for (Brick brick : initialBricks) {
            bricks.add(copyBrick(brick));
        }
    }

    @Override
    public int getRemainingBricks() {
        int count = 0;
        for (Brick brick : bricks) {
            if (!brick.isDestroyed() && brick.getType() != BrickType.UNBREAKABLE) {
                count++;
            }
        }
        return count;
    }

    @Override
    public int getMaxScore() {
        int maxScore = 0;
        for (Brick brick : initialBricks) {
            if (brick.getType() != BrickType.UNBREAKABLE) {
                maxScore += brick.getScore();
            }
        }
        return maxScore;
    }

    /**
     * Helper to backup initial state.
     */
    protected void backupInitialState() {
        initialBricks.clear();
        for (Brick brick : bricks) {
            initialBricks.add(brick); // Store references; deep copy happens in reset
        }
    }

    /**
     * Deep copy a brick instance.
     */
    private Brick copyBrick(Brick original) {
        return new Brick(
                original.getX(),
                original.getY(),
                original.getWidth(),
                original.getHeight(),
                original.getType(),
                parseBrickColor(original)
        );
    }

    /**
     * Derives a color for the copied brick based on its type (placeholder logic).
     */
    private javafx.scene.paint.Color parseBrickColor(Brick brick) {
        // Fallback color selection by type
        switch (brick.getType()) {
            case HARD:
                return javafx.scene.paint.Color.DARKRED;
            case UNBREAKABLE:
                return javafx.scene.paint.Color.GRAY;
            default:
                return javafx.scene.paint.Color.RED;
        }
    }
}