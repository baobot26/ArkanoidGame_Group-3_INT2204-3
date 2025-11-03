package Arkanoid;

import Arkanoid.level.LevelSelectionView;
import Arkanoid.manager.GameManager;
import Arkanoid.model.GameState;
import Arkanoid.view.GameView;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * JavaFX application entry point. Wires together GameManager, GameView,
 * and LevelSelectionView, and drives the main animation loop.
 * FIXED: Memory leak prevention - proper resource management.
 */
public class Main extends Application {
    private GameManager gameManager;
    private GameView gameView;
    private LevelSelectionView levelSelectionView;
    private AnimationTimer gameLoop;
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        // Initialize game manager
        gameManager = new GameManager();

        // Initialize game view
        gameView = new GameView(gameManager);

        // Initialize level selection view
        levelSelectionView = new LevelSelectionView(primaryStage, gameManager.getLevelManager());
        levelSelectionView.setCallback(new LevelSelectionView.LevelSelectionCallback() {
            @Override
            public void onLevelSelected(int levelNumber) {
                // Chọn level và bắt đầu game
                gameManager.selectLevel(levelNumber);
                showGameView();
            }

            @Override
            public void onBack() {
                // Quay về menu
                gameManager.setCurrentState(GameState.MENU);
                showGameView();
            }
        });

        // Set callback cho InputHandler để mở Level Selection (khi nhấn L hoặc ESC)
        gameView.getInputHandler().setOnShowLevelSelection(this::showLevelSelection);

        // Set up stage
        primaryStage.setTitle("Arkanoid Game");
        showGameView();
        primaryStage.setResizable(false);
        primaryStage.show();

        // Start game loop
        startGameLoop();
    }

    private void showGameView() {
        primaryStage.setScene(gameView.getScene());
        primaryStage.setTitle("Arkanoid Game");

        // Gán lại callback mỗi khi quay về game view (menu hoặc gameplay)
        gameView.getInputHandler().setOnShowLevelSelection(this::showLevelSelection);
    }

    private void showLevelSelection() {
        // ⚠️ CRITICAL: Cleanup trước khi mở level selection
        gameManager.cleanup();

        // Enter MENU state and play title music when opening level selection
        gameManager.showLevelSelection();
        levelSelectionView.refresh();
        levelSelectionView.show();
        primaryStage.setTitle("Arkanoid - Level Selection");
    }

    private void startGameLoop() {
        final long[] lastUpdate = {System.nanoTime()};

        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                double deltaTime = (now - lastUpdate[0]) / 1_000_000_000.0;
                lastUpdate[0] = now;

                deltaTime = Math.min(deltaTime, 0.05);

                // ⚠️ CRITICAL: Chỉ update khi ở GameView VÀ đang PLAYING
                if (primaryStage.getScene() == gameView.getScene()) {
                    gameManager.update(deltaTime);
                    gameView.render(gameManager);
                }
            }
        };

        gameLoop.start();
    }

    @Override
    public void stop() {
        System.out.println("🛑 Application stopping...");

        if (gameLoop != null) {
            gameLoop.stop();
        }

        if (gameManager != null) {
            gameManager.shutdown();
        }

        System.out.println("✅ Application stopped cleanly");
    }

    public static void main(String[] args) {
        launch(args);
    }
}