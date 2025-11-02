package Arkanoid.renderer;

import Arkanoid.level.Level;
import Arkanoid.manager.GameManager;
import Arkanoid.manager.ScoreManager;
import Arkanoid.model.*;
import Arkanoid.util.Constants;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

/**
 * Renders the entire game based on the current {@link Arkanoid.model.GameState}.
 * Draws playfield entities (bricks, power-ups, paddle, balls) and HUD/overlays.
 */
public class Renderer {
    private GraphicsContext gc;

    /**
     * Creates a renderer that draws to the given GraphicsContext.
     */
    public Renderer(GraphicsContext gc) {
        this.gc = gc;
    }

    /**
     * Renders the entire frame based on the current GameState.
     * Draws world entities and overlays (menu/pause/game over/level complete).
     */
    public void render(GameManager gameManager) {
        // Clear screen
        gc.setFill(Constants.BACKGROUND_COLOR);
        gc.fillRect(0, 0, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);

        GameState state = gameManager.getCurrentState();

        switch (state) {
            case MENU:
                renderMenu();
                break;
            case PLAYING:
            case PAUSED:
                renderGame(gameManager);
                if (state == GameState.PAUSED) {
                    renderPauseOverlay();
                }
                break;
            case GAME_OVER:
                renderGame(gameManager);
                renderGameOver(gameManager.getScoreManager());
                break;
            case LEVEL_COMPLETE:
                renderGame(gameManager);
                renderLevelComplete(gameManager);
                break;
        }
    }

    private void renderGame(GameManager gameManager) {
        // Render bricks
        for (Brick brick : gameManager.getBricks()) {
            brick.render(gc);
        }

        // Render power-ups
        for (PowerUps powerUp : gameManager.getPowerUps()) {
            powerUp.render(gc);
        }

        // Render paddle
        gameManager.getPaddle().render(gc);

        // Render balls
        for (Ball ball : gameManager.getBalls()) {
            ball.render(gc);
        }

        // Render UI
        renderUI(gameManager);
    }

    private void renderUI(GameManager gameManager) {
        ScoreManager scoreManager = gameManager.getScoreManager();
        Level currentLevel = gameManager.getCurrentLevel();

        gc.setFill(Constants.UI_TEXT_COLOR);
        gc.setFont(Font.font("Arial", Constants.UI_FONT_SIZE));

        // Score
        gc.setTextAlign(TextAlignment.LEFT);
        gc.fillText("Score: " + scoreManager.getScore(), 10, 25);

        // Level Name (Center)
        gc.setTextAlign(TextAlignment.CENTER);
        if (currentLevel != null) {
            gc.fillText(currentLevel.getLevelName() + " (" +
                            currentLevel.getLevelNumber() + "/" +
                            gameManager.getLevelManager().getTotalLevels() + ")",
                    Constants.WINDOW_WIDTH / 2.0, 25);
        } else {
            gc.fillText("Level " + scoreManager.getLevel(),
                    Constants.WINDOW_WIDTH / 2.0, 25);
        }

        // Lives (Right)
        gc.setTextAlign(TextAlignment.RIGHT);
        gc.fillText("Lives: " + scoreManager.getLives(),
                Constants.WINDOW_WIDTH - 10, 25);
    }

    private void renderMenu() {
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", 60));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText("ARKANOID", Constants.WINDOW_WIDTH / 2.0, 150);

        gc.setFont(Font.font("Arial", 30));
        gc.fillText("Press SPACE to Start", Constants.WINDOW_WIDTH / 2.0, 250);

        gc.setFill(Color.CYAN);
        gc.setFont(Font.font("Arial", 26));
        gc.fillText("Press L for Level Selection", Constants.WINDOW_WIDTH / 2.0, 300);

        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", 20));
        gc.fillText("Controls:", Constants.WINDOW_WIDTH / 2.0, 360);
        gc.setFont(Font.font("Arial", 16));
        gc.fillText("LEFT/RIGHT or A/D - Move Paddle", Constants.WINDOW_WIDTH / 2.0, 390);
        gc.fillText("SPACE - Launch Ball", Constants.WINDOW_WIDTH / 2.0, 415);
        gc.fillText("P - Pause Game", Constants.WINDOW_WIDTH / 2.0, 440);
        gc.fillText("ESC - Return to Menu", Constants.WINDOW_WIDTH / 2.0, 465);

        // Draw power-up legend
        gc.setFont(Font.font("Arial", 16));
        gc.fillText("Power-ups:", Constants.WINDOW_WIDTH / 2.0, 500);

        int y = 520;
        String[] powerUpInfo = {
                "🟢 Expand Paddle  🔴 Shrink Paddle",
                "🟡 Speed Up Ball  🔵 Slow Down Ball",
                "💖 Extra Life  🟠 Multi Ball"
        };

        gc.setFont(Font.font("Arial", 14));
        for (String info : powerUpInfo) {
            gc.fillText(info, Constants.WINDOW_WIDTH / 2.0, y);
            y += 20;
        }
    }

    private void renderPauseOverlay() {
        gc.setFill(Color.rgb(0, 0, 0, 0.7));
        gc.fillRect(0, 0, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);

        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", 48));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText("PAUSED", Constants.WINDOW_WIDTH / 2.0, Constants.WINDOW_HEIGHT / 2.0 - 20);

        gc.setFont(Font.font("Arial", 20));
        gc.fillText("Press P to Resume", Constants.WINDOW_WIDTH / 2.0, Constants.WINDOW_HEIGHT / 2.0 + 30);
        gc.fillText("Press ESC for Menu", Constants.WINDOW_WIDTH / 2.0, Constants.WINDOW_HEIGHT / 2.0 + 60);
    }

    private void renderGameOver(ScoreManager scoreManager) {
        gc.setFill(Color.rgb(0, 0, 0, 0.8));
        gc.fillRect(0, 0, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);

        gc.setFill(Color.RED);
        gc.setFont(Font.font("Arial", 60));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText("GAME OVER", Constants.WINDOW_WIDTH / 2.0, 250);

        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", 30));
        gc.fillText("Final Score: " + scoreManager.getScore(), Constants.WINDOW_WIDTH / 2.0, 320);
        gc.fillText("High Score: " + scoreManager.getHighScore(), Constants.WINDOW_WIDTH / 2.0, 360);

        gc.setFont(Font.font("Arial", 20));
        gc.fillText("Press SPACE to Try Again", Constants.WINDOW_WIDTH / 2.0, 420);
        gc.fillText("Press ESC for Menu", Constants.WINDOW_WIDTH / 2.0, 450);
    }

    private void renderLevelComplete(GameManager gameManager) {
        ScoreManager scoreManager = gameManager.getScoreManager();
        Level currentLevel = gameManager.getCurrentLevel();

        gc.setFill(Color.rgb(0, 0, 0, 0.8));
        gc.fillRect(0, 0, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);

        gc.setFill(Color.GOLD);
        gc.setFont(Font.font("Arial", 60));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText("LEVEL COMPLETE!", Constants.WINDOW_WIDTH / 2.0, 220);

        gc.setFill(Color.LIGHTGREEN);
        gc.setFont(Font.font("Arial", 30));
        if (currentLevel != null) {
            gc.fillText(currentLevel.getLevelName(), Constants.WINDOW_WIDTH / 2.0, 270);
        }

        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", 28));
        gc.fillText("Score: " + scoreManager.getScore(), Constants.WINDOW_WIDTH / 2.0, 330);

        // Progress bar
        int totalLevels = gameManager.getLevelManager().getTotalLevels();
        int currentLevelNum = gameManager.getLevelManager().getCurrentLevelNumber();

        gc.setFont(Font.font("Arial", 18));
        gc.fillText("Progress: " + currentLevelNum + "/" + totalLevels,
                Constants.WINDOW_WIDTH / 2.0, 370);

        // Draw progress bar
        double barWidth = 300;
        double barHeight = 20;
        double barX = (Constants.WINDOW_WIDTH - barWidth) / 2.0;
        double barY = 380;

        gc.setStroke(Color.WHITE);
        gc.setLineWidth(2);
        gc.strokeRect(barX, barY, barWidth, barHeight);

        double progress = (double) currentLevelNum / totalLevels;
        gc.setFill(Color.GOLD);
        gc.fillRect(barX + 2, barY + 2, (barWidth - 4) * progress, barHeight - 4);

        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", 20));

        if (currentLevelNum < totalLevels) {
            gc.fillText("Press SPACE for Next Level", Constants.WINDOW_WIDTH / 2.0, 440);
        } else {
            gc.setFill(Color.GOLD);
            gc.setFont(Font.font("Arial", 26));
            gc.fillText("🎉 ALL LEVELS COMPLETED! 🎉", Constants.WINDOW_WIDTH / 2.0, 440);
            gc.setFill(Color.WHITE);
            gc.setFont(Font.font("Arial", 20));
            gc.fillText("Press SPACE to Play Again", Constants.WINDOW_WIDTH / 2.0, 480);
        }

        gc.fillText("Press ESC for Menu", Constants.WINDOW_WIDTH / 2.0, 520);
    }
}