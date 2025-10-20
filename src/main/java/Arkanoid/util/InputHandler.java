package Arkanoid.util;

import Arkanoid.manager.GameManager;
import Arkanoid.model.GameState;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class InputHandler {
    private GameManager gameManager;

    public InputHandler(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public void handleKeyPressed(KeyEvent event) {
        KeyCode code = event.getCode();
        GameState state = gameManager.getCurrentState();

        switch (state) {
            case MENU:
                handleMenuInput(code);
                break;
            case PLAYING:
                handlePlayingInput(code, true);
                break;
            case PAUSED:
                handlePausedInput(code);
                break;
            case GAME_OVER:
                handleGameOverInput(code);
                break;
            case LEVEL_COMPLETE:
                handleLevelCompleteInput(code);
                break;
        }
    }

    public void handleKeyReleased(KeyEvent event) {
        KeyCode code = event.getCode();

        if (gameManager.getCurrentState() == GameState.PLAYING) {
            handlePlayingInput(code, false);
        }
    }

    private void handleMenuInput(KeyCode code) {
        if (code == KeyCode.SPACE) {
            gameManager.startGame();
        }
    }

    private void handlePlayingInput(KeyCode code, boolean pressed) {
        switch (code) {
            case LEFT:
            case A:
                gameManager.getPaddle().setMovingLeft(pressed);
                break;
            case RIGHT:
            case D:
                gameManager.getPaddle().setMovingRight(pressed);
                break;
            case SPACE:
                if (pressed) {
                    gameManager.launchBall();
                }
                break;
            case P:
                if (pressed) {
                    gameManager.pauseGame();
                }
                break;
            case ESCAPE:
                if (pressed) {
                    gameManager.setCurrentState(GameState.MENU);
                }
                break;
        }
    }

    private void handlePausedInput(KeyCode code) {
        if (code == KeyCode.P) {
            gameManager.pauseGame();
        } else if (code == KeyCode.ESCAPE) {
            gameManager.setCurrentState(GameState.MENU);
        }
    }

    private void handleGameOverInput(KeyCode code) {
        if (code == KeyCode.SPACE) {
            gameManager.startGame();
        } else if (code == KeyCode.ESCAPE) {
            gameManager.setCurrentState(GameState.MENU);
        }
    }

    private void handleLevelCompleteInput(KeyCode code) {
        if (code == KeyCode.SPACE) {
            gameManager.nextLevel();
        }
    }
}