package Arkanoid.manager;

import Arkanoid.model.*;

import java.util.List;
import java.util.Iterator;

public class CollisionManager {
    private GameManager gameManager;

    public CollisionManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }
    public void checkBallPaddleCollision() {
        Paddle paddle = gameManager.getPaddle();
        for (Ball ball : gameManager.getBalls()) {
            if (ball.intersects(paddle)) {
                // Simple bounce for now
                ball.setVelocityY(-Math.abs(ball.getVelocityY()));
                //TODO: Angle based on where it hits the paddle
            }
        }
    }

    public Brick checkBallBrickCollision() {
    }

    public PowerUps checkPaddlePowerUpCollision() {
        Paddle paddle = gameManager.getPaddle();
        Iterator<PowerUps> powerUps = gameManager.getPowerUps().iterator();
        while (powerUps.hasNext()) {
        PowerUps powerUp = powerUps.next();
        if (!powerUp.isCollected() && powerUp.intersects(paddle)) {
            powerUp.collect();
            gameManager.applyPowerUp(powerUp.getType());
            powerUps.remove(); // Azusuki: remove from pool of active power-ups
            return powerUp;
        }
    }
        return null;
    }
}