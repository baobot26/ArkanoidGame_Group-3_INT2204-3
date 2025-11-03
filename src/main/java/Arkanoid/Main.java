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
    // Show the main menu (GameView renders MENU state by default)
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
    // Enter MENU state and play title music when opening level selection
    gameManager.showLevelSelection();
        levelSelectionView.refresh(); // Cập nhật trạng thái mở khóa
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

                deltaTime = Math.min(deltaTime, 0.05); // giới hạn khung hình

                // Cập nhật logic game
                gameManager.update(deltaTime);

                // 🔹 Không tự động mở Level Selection nữa
                // Người chơi chỉ vào qua phím L hoặc ESC (InputHandler quản lý)

                // 🔹 Render nếu đang ở GameView
                if (primaryStage.getScene() == gameView.getScene()) {
                    gameView.render(gameManager);
                }
            }
        };

        gameLoop.start();
    }

    @Override
    public void stop() {
        if (gameLoop != null) {
            gameLoop.stop();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
