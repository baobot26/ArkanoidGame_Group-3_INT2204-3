package Arkanoid.manager;

import Arkanoid.model.*;
import Arkanoid.util.Constants;
import java.util.*;
//TEAM NOTE: (Azusuki) Paddle logic added


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
        initializeGame();
    }
//Azusuki: initializeGame method created to set up initial game state and objects
    private void initializeGame() {
        currentState = GameState.MENU;
        paddle = new Paddle();
        balls = new ArrayList<>();
        bricks = new ArrayList<>();
        powerUps = new ArrayList<>();
        collisionManager = new CollisionManager(this);
        scoreManager = new ScoreManager();
        random = new Random();
        createLevel();
        resetBall();
    }

    private void createLevel() {
    }

    private BrickType determineBrickType() {
        return null;
    }
// Azusuki: update method modified to include paddle and power-up updates, as well as collision checks
    public void update() {
        //Azusuki: Only update game objects if the game is in PLAYING state
        if (currentState == GameState.PLAYING) {
            paddle.update();
            for(Ball ball : balls) {
                ball.update();
            }
            for (PowerUps powerUps : powerUps) {
                powerUps.update();
            }
            checkCollisions();
        }
    }

    private void checkCollisions() {
        collisionManager.checkBallPaddleCollision();
        collisionManager.checkBallBrickCollision();
        collisionManager.checkPaddlePowerUpCollision();
    }

    private void spawnPowerUp() {
    }
// Azusuki: Paddle shrink and expand power-ups logic added
    // Azusuki: changed to public to be accessed from CollisionManager
    public void applyPowerUp(PowerUpType type) {
        switch (type) {
            case EXPAND_PADDLE:
                paddle.expand();
                break;
            case SHRINK_PADDLE:
                paddle.shrink();
                break;
        }
    }

    private boolean isLevelComplete() {
        return false;
    }

    public void startGame() {
        currentState = GameState.PLAYING;
    }

    public void pauseGame() {
        currentState = GameState.PAUSED;
    }

    public void nextLevel() {
        createLevel();
        resetLevel();
    }
//Azusuki: paddle and ball is reset at level start
    private void resetLevel() {
        paddle.reset();
        resetBall();
    }

    private void resetBall() {
    }

    public void launchBall() {
    }

    // Getters & Setters
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
