package Arkanoid.level;

import java.util.List;

/**
 * Plain data object describing a level configuration loaded from JSON.
 * Contains metadata (number, name), optional per-level parameters (ball speed, lives),
 * background image path, and a list of brick descriptors.
 */
public class LevelData {
    private int levelNumber;
    private String name;
    private List<BrickData> bricks;
    private double ballSpeed;
    private int lives;

    // ✅ Background image path (optional)
    private String backgroundImage;

    // Constructor
    public LevelData() {
        this.ballSpeed = 4.5; // Default
        this.lives = 3; // Default
        this.backgroundImage = null; // Default: no custom background
    }

    public LevelData(int levelNumber, String name, List<BrickData> bricks) {
        this.levelNumber = levelNumber;
        this.name = name;
        this.bricks = bricks;
        this.ballSpeed = 4.5;
        this.lives = 3;
        this.backgroundImage = null;
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
     * ✅ Gets the background image path for this level.
     * @return path to image (e.g., "/images/level/space.png") or null for default
     */
    public String getBackgroundImage() {
        return backgroundImage;
    }

    /**
     * ✅ Sets the background image path for this level.
     * @param backgroundImage path to image resource
     */
    public void setBackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    /**
     * Inner class describing a single brick cell in the level grid.
     */
    public static class BrickData {
        private int row;
        private int col;
        private String type; // "NORMAL", "HARD", "UNBREAKABLE"
        private String color; // Hex color like "#FF0000"

        // Optional motion config (for dynamic bricks)
        private Boolean moving;         // default false
        private String direction;       // "HORIZONTAL" or "VERTICAL"
        private Double speed;           // pixels per second
        private Double range;           // max offset from origin in pixels

        // Constructor
        public BrickData() {}

        public BrickData(int row, int col, String type, String color) {
            this.row = row;
            this.col = col;
            this.type = type;
            this.color = color;
            this.moving = false;
            this.direction = null;
            this.speed = null;
            this.range = null;
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

        // Motion getters/setters
        public Boolean getMoving() { return moving; }
        public void setMoving(Boolean moving) { this.moving = moving; }
        public String getDirection() { return direction; }
        public void setDirection(String direction) { this.direction = direction; }
        public Double getSpeed() { return speed; }
        public void setSpeed(Double speed) { this.speed = speed; }
        public Double getRange() { return range; }
        public void setRange(Double range) { this.range = range; }
    }
}