package Arkanoid.manager;

import Arkanoid.level.Level;
import Arkanoid.level.LevelManager;
import Arkanoid.model.*;
import Arkanoid.util.Constants;
import Arkanoid.audio.SoundManager;
import java.util.Timer;
import java.util.TimerTask;
import java.util.*;

/**
 * Central coordinator for game state, entities and level progression.
 * Owns paddle/balls/bricks/power-ups, updates collisions and scoring,
 * and communicates with {@link Arkanoid.level.LevelManager} to load and advance levels.
 */
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
    int detected = Arkanoid.level.LevelLoader.countAvailableLevels(50);
    if (detected <= 0) detected = 3; // fallback
    this.levelManager.loadLevels(detected);

    // Load sounds and apply saved settings
    SoundManager.getInstance().loadDefaultSounds();
    Arkanoid.audio.AudioSetting.getInstance().loadSettings();
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
     * Loads the current level from LevelManager and applies level configuration.
     */
    private void loadCurrentLevel() {
        currentLevel = levelManager.getCurrentLevel();

        if (currentLevel != null) {
            System.out.println("Loading level: " + currentLevel.getLevelName() +
                    " (Level " + currentLevel.getLevelNumber() + ")");

            // Level is already initialized in LevelManager.loadLevels()

            // Load bricks from level
            bricks.clear();
            bricks.addAll(currentLevel.getBricks());

            System.out.println("Loaded " + bricks.size() + " bricks from level data");
                // Apply level configuration: ball speed and lives
                try {
                    double levelBallSpeed = currentLevel.getBallSpeed();
                    int levelLives = currentLevel.getInitialLives();

                    // Update lives if provided
                    if (levelLives > 0) {
                        scoreManager.setLives(levelLives);
                    }

                    // Update ball speed for all existing balls
                    for (Ball b : balls) {
                        b.setBaseSpeed(levelBallSpeed);
                    }
                } catch (Exception ignored) {
                    // keep defaults on malformed level data
                }

            // Debug: Print first few bricks
            for (int i = 0; i < Math.min(3, bricks.size()); i++) {
                Brick b = bricks.get(i);
                System.out.println("   Brick " + i + ": type=" + b.getType() +
                        ", pos=(" + b.getX() + "," + b.getY() + ")");
            }
        } else {
            System.out.println("Current level is NULL! Using legacy level generation");
            // Fallback: create legacy level if loading failed
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

    /**
     * Advances the game simulation by deltaTime when in PLAYING state.
     * Updates paddle, balls, power-ups, checks collisions and level completion.
     */
    public void update(double deltaTime) {
        if (currentState != GameState.PLAYING) return;

        paddle.update(deltaTime);

        // Update bricks (for moving bricks)
        for (Brick brick : bricks) {
            brick.update(deltaTime);
        }

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
                        // Play game over music to completion and stop ambient/effects
                        Arkanoid.audio.SoundManager sm = Arkanoid.audio.SoundManager.getInstance();
                        sm.stopAll();
                        sm.playSound("music_gameover");
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
                Arkanoid.audio.SoundManager.getInstance().playSound("effect_brick");
                Arkanoid.audio.SoundManager.getInstance().playSound("effect_score");

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

    /**
     * Starts a new game from level 1 and enters PLAYING state.
     */
    public void startGame() {
        currentState = GameState.PLAYING;
        scoreManager.reset();
        levelManager.restartGame(); // Reset về level 1
    initializeGame();
    // Stage start: play start music for ~5 seconds
    SoundManager sm = SoundManager.getInstance();
    sm.stopAll();
    sm.playSound("music_stage_start");
    scheduleStageStartStop();
    }

    /**
     * Toggles between PLAYING and PAUSED states.
     */
    public void pauseGame() {
        if (currentState == GameState.PLAYING) currentState = GameState.PAUSED;
        else if (currentState == GameState.PAUSED) currentState = GameState.PLAYING;
    }

    /**
     * Advances to the next level if available, otherwise sets GAME_OVER when all levels are finished.
     */
    public void nextLevel() {
        boolean hasNextLevel = levelManager.nextLevel();

    if (hasNextLevel) {
            scoreManager.nextLevel();
            // Ensure we reference the new current level from LevelManager
            currentLevel = levelManager.getCurrentLevel();
            resetLevel();
            currentState = GameState.PLAYING;
            // Play short stage start jingle on level advance
            SoundManager sm = SoundManager.getInstance();
            sm.playSound("music_stage_start");
            scheduleStageStartStop();
        } else {
            // Hết level - game hoàn thành
            currentState = GameState.GAME_OVER;
            System.out.println("Congratulations! You completed all levels!");
            // Play title when player wins (until completion)
            SoundManager sm = SoundManager.getInstance();
            sm.stopAll();
            sm.playSound("music_title");
        }
    }

    private void resetLevel() {
        // Always refresh the current level from the manager to avoid stale reference
        if (levelManager != null) {
            currentLevel = levelManager.getCurrentLevel();
        }
        paddle.reset();
        // Clear movement flags to avoid drift when entering a new level
        paddle.setMovingLeft(false);
        paddle.setMovingRight(false);
        balls.clear();
        Ball newBall = new Ball(paddle);
        // Áp dụng tốc độ bóng theo level hiện tại
        if (currentLevel != null) {
            newBall.setBaseSpeed(currentLevel.getBallSpeed());
        }
        balls.add(newBall);
        powerUps.clear();
        activePowerUps.clear();

        // Reset bricks về trạng thái ban đầu
        if (currentLevel != null) {
            currentLevel.reset(); // Re-initialize from LevelData
            bricks.clear();
            bricks.addAll(currentLevel.getBricks());
            System.out.println("Reset level: " + currentLevel.getLevelName());
            // Reset lives from level when resetting
            scoreManager.setLives(currentLevel.getInitialLives());
        } else {
            loadCurrentLevel();
        }
    }

    private void resetBall() {
        balls.clear();
        balls.add(new Ball(paddle));
    }

    /**
     * Launches any balls currently stuck to the paddle.
     */
    public void launchBall() {
        for (Ball ball : balls) {
            if (ball.isStuck()) ball.launch();
        }
    }

    /**
     * Selects a specific level by 1-based index and resets state to play it.
     * Intended to be invoked by the Level Selection UI.
     */
    public void selectLevel(int levelNumber) {
        if (levelManager.selectLevel(levelNumber)) {
            currentLevel = levelManager.getCurrentLevel(); // update current level
            resetLevel();                                  // reload bricks for the new level
            currentState = GameState.PLAYING;
            System.out.println("Selected Level " + currentLevel.getLevelNumber() + ": " + currentLevel.getLevelName());
        } else {
            System.out.println("Cannot select level " + levelNumber + " (does not exist)");
        }
    }


    /**
     * Shows the level selection/menu state.
     */
    public void showLevelSelection() {
        currentState = GameState.MENU;
    SoundManager sm = SoundManager.getInstance();
    sm.stopAll();
    sm.playSound("music_title");
    }

    /**
     * Ensure stage start jingle plays for exactly 5 seconds, then stop it
     * and optionally start ambient background.
     */
    private void scheduleStageStartStop() {
        Timer timer = new Timer(true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                SoundManager sm = SoundManager.getInstance();
                sm.stopSound("music_stage_start");
                // Start alternating background and ambient after jingle (ambient is much quieter)
                sm.startBackgroundAlternating();
                sm.playSound("ambient_bg");
            }
        }, 5000);
    }

    // Getters
    /** @return the current high-level game state (menu, playing, etc.). */
    public GameState getCurrentState() { return currentState; }
    /** @return the player paddle instance. */
    public Paddle getPaddle() { return paddle; }
    /** @return a live list of active balls. */
    public List<Ball> getBalls() { return balls; }
    /** @return a live list of bricks in the current level. */
    public List<Brick> getBricks() { return bricks; }
    /** @return a live list of active power-ups. */
    public List<PowerUps> getPowerUps() { return powerUps; }
    /** @return the score manager tracking score, lives and level index. */
    public ScoreManager getScoreManager() { return scoreManager; }
    /** @return the level manager used to load and navigate levels. */
    public LevelManager getLevelManager() { return levelManager; }
    /** @return the currently loaded level or null if using legacy fallback. */
    public Level getCurrentLevel() { return currentLevel; }

    /**
     * Sets the high-level game state (e.g., MENU, PLAYING, PAUSED, etc.).
     */
    public void setCurrentState(GameState gameState) {
        this.currentState = gameState;
    }
}