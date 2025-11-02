package Arkanoid.level;

import Arkanoid.model.Brick;
import Arkanoid.model.BrickType;
import Arkanoid.util.Constants;
import javafx.scene.paint.Color;

/**
 * Concrete implementation của Level
 * Khởi tạo level từ LevelData
 */
public class Level extends AbstractLevel {
    private LevelData levelData;

    public Level(LevelData levelData) {
        super(levelData.getLevelNumber(), levelData.getName());
        this.levelData = levelData;
    }

    @Override
    public void initialize() {
        bricks.clear();

        if (levelData.getBricks() != null) {
            // Load từ LevelData
            for (LevelData.BrickData brickData : levelData.getBricks()) {
                Brick brick = createBrickFromData(brickData);
                bricks.add(brick);
            }
        } else {
            // Fallback: tạo level mặc định nếu không có data
            createDefaultLevel();
        }

        // Backup initial state để reset về sau
        backupInitialState();

        System.out.println("   📦 Initialized " + bricks.size() + " bricks");
    }

    @Override
    public void reset() {
        // Re-initialize level từ levelData
        initialize();
    }

    /**
     * Tạo brick từ BrickData
     */
    private Brick createBrickFromData(LevelData.BrickData data) {
        // Tính tọa độ dựa trên row và col
        double x = Constants.BRICK_OFFSET_X + data.getCol() * (Constants.BRICK_WIDTH + Constants.BRICK_PADDING);
        double y = Constants.BRICK_OFFSET_Y + data.getRow() * (Constants.BRICK_HEIGHT + Constants.BRICK_PADDING);

        // Parse type
        BrickType type = parseBrickType(data.getType());

        // Parse color
        Color color = parseColor(data.getColor());

        return new Brick(x, y, Constants.BRICK_WIDTH, Constants.BRICK_HEIGHT, type, color);
    }

    /**
     * Parse string type thành BrickType enum
     */
    private BrickType parseBrickType(String typeStr) {
        if (typeStr == null) return BrickType.NORMAL;

        switch (typeStr.toUpperCase()) {
            case "HARD":
                return BrickType.HARD;
            case "UNBREAKABLE":
                return BrickType.UNBREAKABLE;
            default:
                return BrickType.NORMAL;
        }
    }

    /**
     * Parse hex color string thành Color
     */
    private Color parseColor(String colorStr) {
        if (colorStr == null || colorStr.isEmpty()) {
            return Constants.BRICK_COLORS[0]; // Default red
        }

        try {
            return Color.web(colorStr);
        } catch (Exception e) {
            return Constants.BRICK_COLORS[0];
        }
    }

    /**
     * Tạo level mặc định nếu không có data
     */
    private void createDefaultLevel() {
        for (int row = 0; row < Constants.BRICK_ROWS; row++) {
            for (int col = 0; col < Constants.BRICK_COLS; col++) {
                double x = Constants.BRICK_OFFSET_X + col * (Constants.BRICK_WIDTH + Constants.BRICK_PADDING);
                double y = Constants.BRICK_OFFSET_Y + row * (Constants.BRICK_HEIGHT + Constants.BRICK_PADDING);

                Color color = Constants.BRICK_COLORS[row % Constants.BRICK_COLORS.length];
                BrickType type = BrickType.NORMAL;

                bricks.add(new Brick(x, y, Constants.BRICK_WIDTH, Constants.BRICK_HEIGHT, type, color));
            }
        }
    }

    /**
     * Lấy ball speed của level này
     */
    public double getBallSpeed() {
        return levelData.getBallSpeed();
    }

    /**
     * Lấy số lives ban đầu của level
     */
    public int getInitialLives() {
        return levelData.getLives();
    }
}