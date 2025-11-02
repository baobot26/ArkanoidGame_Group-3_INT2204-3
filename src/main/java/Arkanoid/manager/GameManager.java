package Arkanoid.manager;

import Arkanoid.level.Level;
import Arkanoid.level.LevelManager;
import Arkanoid.model.*;
import Arkanoid.util.Constants;
import java.util.*;

public class GameManager {
    private GameState currentState;
    private Paddle paddle;
    private List<Ball> balls;
    private List<Brick> bricks;
    private List<PowerUps> powerUps;
    private CollisionManager collisionManager;
    private ScoreManager scoreManager;
    private Random random;

    // Level Management
    private LevelManager levelManager;
    private Level currentLevel;

    // Quản lý thời gian hiệu lực của PowerUps
    private Map<PowerUpType, Double> activePowerUps;

    public GameManager() {
        this.currentState = GameState.MENU;
        this.collisionManager = new CollisionManager();
        this.scoreManager = new ScoreManager();
        this.random = new Random();
        this.activePowerUps = new HashMap<>();

        // Initialize Level Manager
        this.levelManager = new LevelManager();
        this.levelManager.loadLevels(3); // Load 3 levels

        initializeGame();
    }

    private void initializeGame() {
        paddle = new Paddle();
        balls = new ArrayList<>();
        balls.add(new Ball(paddle));
        bricks = new ArrayList<>();
        powerUps = new ArrayList<>();

        loadCurrentLevel();
    }

    /**
     * Load level hiện tại từ LevelManager
     */
    private void loadCurrentLevel() {
        currentLevel = levelManager.getCurrentLevel();

        if (currentLevel != null) {
            System.out.println("🎮 Loading level: " + currentLevel.getLevelName() +
                    " (Level " + currentLevel.getLevelNumber() + ")");

            // Initialize level (không reset để tránh double init)
            // Level đã được initialize trong LevelManager.loadLevels()

            // Load bricks từ level
            bricks.clear();
            bricks.addAll(currentLevel.getBricks());

            System.out.println("   ✅ Loaded " + bricks.size() + " bricks from level data");

            // Debug: In ra vài brick đầu tiên
            for (int i = 0; i < Math.min(3, bricks.size()); i++) {
                Brick b = bricks.get(i);
                System.out.println("   Brick " + i + ": type=" + b.getType() +
                        ", pos=(" + b.getX() + "," + b.getY() + ")");
            }
        } else {
            System.out.println("⚠️ Current level is NULL! Using legacy level generation");
            // Fallback: tạo level cũ nếu không load được
            createLegacyLevel();
        }
    }

    /**
     * Tạo level theo cách cũ (fallback)
     */
    private void createLegacyLevel() {
        bricks.clear();
        int level = scoreManager.getLevel();

        for (int row = 0; row < Constants.BRICK_ROWS; row++) {
            for (int col = 0; col < Constants.BRICK_COLS; col++) {
                double x = Constants.BRICK_OFFSET_X + col * (Constants.BRICK_WIDTH + Constants.BRICK_PADDING);
                double y = Constants.BRICK_OFFSET_Y + row * (Constants.BRICK_HEIGHT + Constants.BRICK_PADDING);

                BrickType type = determineBrickType(row, level);
                Brick brick = new Brick(
                        x, y,
                        Constants.BRICK_WIDTH,
                        Constants.BRICK_HEIGHT,
                        type,
                        Constants.BRICK_COLORS[row % Constants.BRICK_COLORS.length]
                );
                bricks.add(brick);
            }
        }
    }

    private BrickType determineBrickType(int row, int level) {
        int chance = random.nextInt(100);
        if (level > 3 && row < 2 && chance < 20)
            return BrickType.UNBREAKABLE;
        else if (level > 1 && chance < 30)
            return BrickType.HARD;
        return BrickType.NORMAL;
    }

    public void update(double deltaTime) {
        if (currentState != GameState.PLAYING) return;

        paddle.update(deltaTime);

        // Update bóng
        Iterator<Ball> ballIterator = balls.iterator();
        while (ballIterator.hasNext()) {
            Ball ball = ballIterator.next();
            ball.update(deltaTime);

            if (ball.isOutOfBounds()) {
                ballIterator.remove();
                if (balls.isEmpty()) {
                    scoreManager.loseLife();
                    if (scoreManager.isGameOver()) {
                        currentState = GameState.GAME_OVER;
                    } else {
                        resetBall();
                    }
                }
            } else {
                checkCollisions(ball);
            }
        }

        // Update PowerUps rơi xuống và va chạm paddle
        Iterator<PowerUps> powerUpIterator = powerUps.iterator();
        while (powerUpIterator.hasNext()) {
            PowerUps powerUp = powerUpIterator.next();
            powerUp.update();

            if (powerUp.isOutOfBounds()) {
                powerUpIterator.remove();
                continue;
            }

            if (!powerUp.isCollected() && paddle.intersects(powerUp)) {
                powerUp.collect();
                applyPowerUp(powerUp.getType());
                scoreManager.addScore(Constants.SCORE_POWERUP);
                powerUpIterator.remove();
            }
        }

        // Kiểm tra hết hạn PowerUp
        updateActivePowerUps();

        if (isLevelComplete()) {
            currentState = GameState.LEVEL_COMPLETE;
        }
    }

    private void checkCollisions(Ball ball) {
        collisionManager.checkBallPaddleCollision(ball, paddle);

        Brick hitBrick = collisionManager.checkBallBrickCollision(ball, bricks);
        if (hitBrick != null) {
            boolean destroyed = hitBrick.hit();
            if (destroyed) {
                scoreManager.addScore(hitBrick.getScore());

                if (random.nextInt(100) < 15) {
                    spawnPowerUp(hitBrick.getCenterX(), hitBrick.getCenterY());
                }
            }
        }
    }

    private void spawnPowerUp(double x, double y) {
        PowerUpType[] types = PowerUpType.values();
        PowerUpType type = types[random.nextInt(types.length)];
        powerUps.add(new PowerUps(x, y, type));
    }

    private void applyPowerUp(PowerUpType type) {
        long now = System.currentTimeMillis();

        switch (type) {
            case EXPAND_PADDLE:
                paddle.expand();
                activePowerUps.put(type, now + Constants.POWERUP_DURATION);
                break;

            case SHRINK_PADDLE:
                paddle.shrink();
                activePowerUps.put(type, now + Constants.POWERUP_DURATION);
                break;

            case SPEED_UP_BALL:
                balls.forEach(Ball::increaseSpeed);
                activePowerUps.put(type, now + Constants.POWERUP_DURATION);
                break;

            case SPEED_DOWN_BALL:
                balls.forEach(Ball::decreaseSpeed);
                activePowerUps.put(type, now + Constants.POWERUP_DURATION);
                break;

            case EXTRA_LIFE:
                scoreManager.addLife();
                break;

            case MULTI_BALL:
                if (balls.size() < 5) {
                    Ball baseBall = balls.get(0);
                    Ball newBall = new Ball(paddle);
                    newBall.setX(baseBall.getX());
                    newBall.setY(baseBall.getY());
                    newBall.setVelocityX(baseBall.getVelocityX() * (random.nextBoolean() ? 1 : -1));
                    newBall.setVelocityY(baseBall.getVelocityY());
                    newBall.launch();
                    balls.add(newBall);
                }
                break;
        }
    }

    private void updateActivePowerUps() {
        long now = System.currentTimeMillis();
        Iterator<Map.Entry<PowerUpType, Double>> iterator = activePowerUps.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<PowerUpType, Double> entry = iterator.next();
            if (now > entry.getValue()) {
                deactivatePowerUp(entry.getKey());
                iterator.remove();
            }
        }
    }

    private void deactivatePowerUp(PowerUpType type) {
        switch (type) {
            case EXPAND_PADDLE:
            case SHRINK_PADDLE:
                paddle.resetSize();
                break;

            case SPEED_UP_BALL:
            case SPEED_DOWN_BALL:
                balls.forEach(Ball::resetSpeed);
                break;

            default:
                break;
        }
    }

    private boolean isLevelComplete() {
        if (currentLevel != null) {
            return currentLevel.isCompleted();
        }

        // Fallback check
        for (Brick brick : bricks) {
            if (!brick.isDestroyed() && brick.getType() != BrickType.UNBREAKABLE) {
                return false;
            }
        }
        return true;
    }

    public void startGame() {
        currentState = GameState.PLAYING;
        scoreManager.reset();
        levelManager.restartGame(); // Reset về level 1
        initializeGame();
    }

    public void pauseGame() {
        if (currentState == GameState.PLAYING) currentState = GameState.PAUSED;
        else if (currentState == GameState.PAUSED) currentState = GameState.PLAYING;
    }

    public void nextLevel() {
        boolean hasNextLevel = levelManager.nextLevel();

        if (hasNextLevel) {
            scoreManager.nextLevel();
            resetLevel();
            currentState = GameState.PLAYING;
        } else {
            // Hết level - game hoàn thành
            currentState = GameState.GAME_OVER;
            System.out.println("🎉 Congratulations! You completed all levels!");
        }
    }

    private void resetLevel() {
        paddle.reset();
        balls.clear();
        balls.add(new Ball(paddle));
        powerUps.clear();
        activePowerUps.clear();

        // Reset bricks về trạng thái ban đầu
        if (currentLevel != null) {
            currentLevel.reset(); // Re-initialize từ LevelData
            bricks.clear();
            bricks.addAll(currentLevel.getBricks());
            System.out.println("🔄 Reset level: " + currentLevel.getLevelName());
        } else {
            loadCurrentLevel();
        }
    }

    private void resetBall() {
        balls.clear();
        balls.add(new Ball(paddle));
    }

    public void launchBall() {
        for (Ball ball : balls) {
            if (ball.isStuck()) ball.launch();
        }
    }

    /**
     * Chọn level cụ thể (từ Level Selection UI)
     */
    /**
     * Chọn level cụ thể (từ Level Selection UI)
     */
    public void selectLevel(int levelNumber) {
        if (levelManager.selectLevel(levelNumber)) {
            currentLevel = levelManager.getCurrentLevel(); // ✅ cập nhật level hiện tại
            resetLevel();                                  // ✅ nạp lại bricks theo level mới
            currentState = GameState.PLAYING;
            System.out.println("✅ Selected Level " + currentLevel.getLevelNumber() + ": " + currentLevel.getLevelName());
        } else {
            System.out.println("⚠️ Không thể chọn level " + levelNumber + " (không tồn tại)");
        }
    }


    /**
     * Mở màn hình chọn level
     */
    public void showLevelSelection() {
        currentState = GameState.MENU;
    }

    // Getters
    public GameState getCurrentState() { return currentState; }
    public Paddle getPaddle() { return paddle; }
    public List<Ball> getBalls() { return balls; }
    public List<Brick> getBricks() { return bricks; }
    public List<PowerUps> getPowerUps() { return powerUps; }
    public ScoreManager getScoreManager() { return scoreManager; }
    public LevelManager getLevelManager() { return levelManager; }
    public Level getCurrentLevel() { return currentLevel; }

    public void setCurrentState(GameState gameState) {
        this.currentState = gameState;
    }
}