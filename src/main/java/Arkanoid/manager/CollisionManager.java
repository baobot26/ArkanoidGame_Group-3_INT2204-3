package Arkanoid.manager;

import Arkanoid.model.Ball;
import Arkanoid.model.Brick;
import Arkanoid.model.Paddle;
import Arkanoid.model.PowerUps;

import java.util.List;

public class CollisionManager {

    public void checkBallPaddleCollision(Ball ball, Paddle paddle) {
        if (ball.intersects(paddle) && ball.getVelocityY() > 0) {
            ball.reverseY();

            // Calculate hit position on paddle (-1 to 1)
            double hitPosition = (ball.getCenterX() - paddle.getCenterX()) / (paddle.getWidth() / 2);
            hitPosition = Math.max(-1, Math.min(1, hitPosition));

            ball.adjustAngle(hitPosition);

            // Move ball above paddle to prevent sticking
            ball.setY(paddle.getY() - ball.getHeight());
        }
    }

    public Brick checkBallBrickCollision(Ball ball, List<Brick> bricks) {
        for (Brick brick : bricks) {
            if (!brick.isDestroyed() && ball.intersects(brick)) {
                // Determine collision side
                double ballCenterX = ball.getCenterX();
                double ballCenterY = ball.getCenterY();
                double brickCenterX = brick.getCenterX();
                double brickCenterY = brick.getCenterY();

                double dx = ballCenterX - brickCenterX;
                double dy = ballCenterY - brickCenterY;

                double width = (ball.getWidth() + brick.getWidth()) / 2;
                double height = (ball.getHeight() + brick.getHeight()) / 2;

                double crossWidth = width * dy;
                double crossHeight = height * dx;

                // Determine collision side and bounce
                if (Math.abs(crossWidth) > Math.abs(crossHeight)) {
                    // Collision from top or bottom
                    ball.reverseY();
                } else {
                    // Collision from left or right
                    ball.reverseX();
                }

                return brick;
            }
        }
        return null;
    }

    public PowerUps checkPaddlePowerUpCollision(Paddle paddle, List<PowerUps> powerUps) {
        for (PowerUps powerUp : powerUps) {
            if (!powerUp.isCollected() && paddle.intersects(powerUp)) {
                powerUp.collect();
                return powerUp;
            }
        }
        return null;
    }
}