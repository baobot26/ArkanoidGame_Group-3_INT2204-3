package Arkanoid.level;

import Arkanoid.model.Brick;
import Arkanoid.model.BrickType;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base class cho tất cả các level
 */
public abstract class AbstractLevel implements LevelInterface {
    protected int levelNumber;
    protected String levelName;
    protected List<Brick> bricks;
    protected List<Brick> initialBricks; // Backup để reset

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
        // Deep copy từ initialBricks
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
     * Helper method để backup initial state
     */
    protected void backupInitialState() {
        initialBricks.clear();
        for (Brick brick : bricks) {
            initialBricks.add(brick); // Chỉ reference, không copy
        }
    }

    /**
     * Deep copy một brick
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
     * Parse color từ brick (placeholder - có thể cải thiện)
     */
    private javafx.scene.paint.Color parseBrickColor(Brick brick) {
        // Lấy color từ Constants dựa trên type
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