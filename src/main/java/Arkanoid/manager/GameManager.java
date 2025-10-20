package Arkanoid.manager;

import Arkanoid.model.*;
import Arkanoid.util.Constants;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class GameManager {
    private GameState currentState;
    private Paddle paddle;
    private List<Ball> balls;
    private List<Brick> bricks;
    private List<PowerUps> powerUps;
    private CollisionManager collisionManager;
    private ScoreManager scoreManager;
    private Random random;

    public GameManager() {
        this.currentState = GameState.MENU;
        this.collisionManager = new CollisionManager();
        this.scoreManager = new ScoreManager();
        this.random = new Random();

        initializeGame();
    }

    private void initializeGame() {
        paddle = new Paddle();
        balls = new ArrayList<>();
        balls.add(new Ball(paddle));
        bricks = new ArrayList<>();
        powerUps = new ArrayList<>();

        createLevel();
    }

    private void createLevel() {
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
        // Higher levels have more hard and unbreakable bricks
        int chance = random.nextInt(100);

        if (level > 3 && row < 2 && chance < 20) {
            return BrickType.UNBREAKABLE;
        } else if (level > 1 && chance < 30) {
            return BrickType.HARD;
        }

        return BrickType.NORMAL;
    }

    public void update(double deltaTime) {
        if (currentState != GameState.PLAYING) {
            return;
        }

        // Update paddle
        paddle.update(deltaTime);

        // Update balls
        Iterator<Ball> ballIterator = balls.iterator();
        while (ballIterator.hasNext()) {
            Ball ball = ballIterator.next();
            ball.update(deltaTime);

            // Check if ball is out of bounds
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
                // Check collisions
                checkCollisions(ball);
            }
        }

        // Update power-ups
        Iterator<PowerUps> powerUpIterator = powerUps.iterator();
        while (powerUpIterator.hasNext()) {
            PowerUps powerUp = powerUpIterator.next();
            powerUp.update();

            if (powerUp.isOutOfBounds() || powerUp.isCollected()) {
                if (powerUp.isCollected()) {
                    applyPowerUp(powerUp.getType());
                    scoreManager.addScore(Constants.SCORE_POWERUP);
                }
                powerUpIterator.remove();
            } else {
                PowerUps collected = collisionManager.checkPaddlePowerUpCollision(paddle, powerUps);
                if (collected != null) {
                    applyPowerUp(collected.getType());
                    scoreManager.addScore(Constants.SCORE_POWERUP);
                }
            }
        }

        // Check if level is complete
        if (isLevelComplete()) {
            currentState = GameState.LEVEL_COMPLETE;
        }
    }

    private void checkCollisions(Ball ball) {
        // Ball-Paddle collision
        collisionManager.checkBallPaddleCollision(ball, paddle);

        // Ball-Brick collision
        Brick hitBrick = collisionManager.checkBallBrickCollision(ball, bricks);
        if (hitBrick != null) {
            boolean destroyed = hitBrick.hit();
            if (destroyed) {
                scoreManager.addScore(hitBrick.getScore());

                // Random chance to spawn power-up
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
        switch (type) {
            case EXPAND_PADDLE:
                paddle.expand();
                break;
            case SHRINK_PADDLE:
                paddle.shrink();
                break;
            case SPEED_UP_BALL:
                balls.forEach(Ball::increaseSpeed);
                break;
            case SPEED_DOWN_BALL:
                balls.forEach(Ball::decreaseSpeed);
                break;
            case EXTRA_LIFE:
                scoreManager.addLife();
                break;
            case MULTI_BALL:
                if (balls.size() < 5) {
                    Ball newBall = new Ball(paddle);
                    newBall.setX(balls.get(0).getX());
                    newBall.setY(balls.get(0).getY());
                    newBall.setVelocityX(balls.get(0).getVelocityX() * (random.nextBoolean() ? 1 : -1));
                    newBall.setVelocityY(balls.get(0).getVelocityY());
                    newBall.launch();
                    balls.add(newBall);
                }
                break;
        }
    }

    private boolean isLevelComplete() {
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
        initializeGame();
    }

    public void pauseGame() {
        if (currentState == GameState.PLAYING) {
            currentState = GameState.PAUSED;
        } else if (currentState == GameState.PAUSED) {
            currentState = GameState.PLAYING;
        }
    }

    public void nextLevel() {
        scoreManager.nextLevel();
        resetLevel();
        currentState = GameState.PLAYING;
    }

    private void resetLevel() {
        paddle.reset();
        balls.clear();
        balls.add(new Ball(paddle));
        powerUps.clear();
        createLevel();
    }

    private void resetBall() {
        balls.clear();
        balls.add(new Ball(paddle));
    }

    public void launchBall() {
        for (Ball ball : balls) {
            if (ball.isStuck()) {
                ball.launch();
            }
        }
    }

    // Getters
    public GameState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(GameState state) {
        this.currentState = state;
    }

    public Paddle getPaddle() {
        return paddle;
    }

    public List<Ball> getBalls() {
        return balls;
    }

    public List<Brick> getBricks() {
        return bricks;
    }

    public List<PowerUps> getPowerUps() {
        return powerUps;
    }

    public ScoreManager getScoreManager() {
        return scoreManager;
    }
}