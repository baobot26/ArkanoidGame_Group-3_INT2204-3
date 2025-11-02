package Arkanoid.level;

import java.util.List;

/**
 * Plain data object describing a level configuration loaded from JSON.
 * Contains metadata (number, name), optional per-level parameters (ball speed, lives),
 * and a list of brick descriptors that the game converts into concrete bricks.
 */
public class LevelData {
    private int levelNumber;
    private String name;
    private List<BrickData> bricks;
    private double ballSpeed;
    private int lives;

    // Constructor
    public LevelData() {
        this.ballSpeed = 4.5; // Default
        this.lives = 3; // Default
    }

    public LevelData(int levelNumber, String name, List<BrickData> bricks) {
        this.levelNumber = levelNumber;
        this.name = name;
        this.bricks = bricks;
        this.ballSpeed = 4.5;
        this.lives = 3;
    }

    // Getters and setters
    public int getLevelNumber() {
        return levelNumber;
    }

    public void setLevelNumber(int levelNumber) {
        this.levelNumber = levelNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<BrickData> getBricks() {
        return bricks;
    }

    public void setBricks(List<BrickData> bricks) {
        this.bricks = bricks;
    }

    public double getBallSpeed() {
        return ballSpeed;
    }

    public void setBallSpeed(double ballSpeed) {
        this.ballSpeed = ballSpeed;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    /**
     * Inner class describing a single brick cell in the level grid.
     */
    public static class BrickData {
        private int row;
        private int col;
        private String type; // "NORMAL", "HARD", "UNBREAKABLE"
        private String color; // Hex color like "#FF0000"

        // Constructor
        public BrickData() {}

        public BrickData(int row, int col, String type, String color) {
            this.row = row;
            this.col = col;
            this.type = type;
            this.color = color;
        }

        // Getters and setters
        public int getRow() {
            return row;
        }

        public void setRow(int row) {
            this.row = row;
        }

        public int getCol() {
            return col;
        }

        public void setCol(int col) {
            this.col = col;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }
    }
}