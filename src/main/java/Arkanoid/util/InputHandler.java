package Arkanoid.util;

import Arkanoid.manager.GameManager;
import Arkanoid.model.GameState;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class InputHandler {
    private final GameManager gameManager;
    private Runnable onShowLevelSelection; // Callback để mở UI chọn level

    public InputHandler(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public void handleKeyPressed(KeyEvent event) {
        KeyCode code = event.getCode();
        GameState state = gameManager.getCurrentState();

        switch (state) {
            case MENU -> handleMenuInput(code);
            case PLAYING -> handlePlayingInput(code, true);
            case PAUSED -> handlePausedInput(code);
            case GAME_OVER -> handleGameOverInput(code);
            case LEVEL_COMPLETE -> handleLevelCompleteInput(code);
        }
    }

    public void handleKeyReleased(KeyEvent event) {
        if (gameManager.getCurrentState() == GameState.PLAYING) {
            handlePlayingInput(event.getCode(), false);
        }
    }

    // ================= MENU =================
    private void handleMenuInput(KeyCode code) {
        switch (code) {
            case SPACE -> gameManager.startGame();
            case L -> {
                // ✅ Luôn cho phép mở lại Level Selection ở menu
                if (onShowLevelSelection != null) {
                    System.out.println("🧩 Opening Level Selection...");
                    onShowLevelSelection.run();
                } else {
                    System.out.println("⚠️ onShowLevelSelection callback is null!");
                }
            }
            case ESCAPE -> {
                // Nếu đang ở menu mà ấn ESC -> thoát hẳn game
                System.out.println("🚪 Exiting game...");
                System.exit(0);
            }
        }
    }

    // ================= PLAYING =================
    private void handlePlayingInput(KeyCode code, boolean pressed) {
        switch (code) {
            case LEFT, A -> gameManager.getPaddle().setMovingLeft(pressed);
            case RIGHT, D -> gameManager.getPaddle().setMovingRight(pressed);
            case SPACE -> {
                if (pressed) gameManager.launchBall();
            }
            case P -> {
                if (pressed) gameManager.pauseGame();
            }
            case ESCAPE -> {
                if (pressed) {
                    // ✅ ESC: trở về menu chính (và hiển thị lại Level Selection)
                    System.out.println("🔙 Returning to MENU from PLAYING...");
                    gameManager.setCurrentState(GameState.MENU);
                    if (onShowLevelSelection != null) {
                        System.out.println("🧩 Opening Level Selection...");
                        onShowLevelSelection.run();
                    }
                }
            }
        }
    }

    // ================= PAUSED =================
    private void handlePausedInput(KeyCode code) {
        switch (code) {
            case P -> gameManager.pauseGame(); // resume
            case ESCAPE -> {
                // ESC từ paused -> về menu
                System.out.println("🔙 Back to MENU from PAUSE");
                gameManager.setCurrentState(GameState.MENU);
                if (onShowLevelSelection != null) {
                    System.out.println("🧩 Opening Level Selection from PAUSE...");
                    onShowLevelSelection.run();
                }
            }
        }
    }

    // ================= GAME OVER =================
    private void handleGameOverInput(KeyCode code) {
        switch (code) {
            case SPACE -> gameManager.startGame();
            case ESCAPE -> {
                System.out.println("🔙 Back to MENU from GAME OVER");
                gameManager.setCurrentState(GameState.MENU);
                if (onShowLevelSelection != null) {
                    System.out.println("🧩 Opening Level Selection from GAME OVER...");
                    onShowLevelSelection.run();
                }
            }
        }
    }

    // ================= LEVEL COMPLETE =================
    private void handleLevelCompleteInput(KeyCode code) {
        switch (code) {
            case SPACE -> gameManager.nextLevel();
            case ESCAPE -> {
                System.out.println("🔙 Back to MENU from LEVEL COMPLETE");
                gameManager.setCurrentState(GameState.MENU);
                if (onShowLevelSelection != null) {
                    System.out.println("🧩 Opening Level Selection from LEVEL COMPLETE...");
                    onShowLevelSelection.run();
                }
            }
        }
    }

    // ================= Callback setter =================
    public void setOnShowLevelSelection(Runnable callback) {
        this.onShowLevelSelection = callback;
    }
}
