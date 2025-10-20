package Arkanoid;

import Arkanoid.manager.GameManager;
import Arkanoid.view.GameView;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    private GameManager gameManager;
    private GameView gameView;
    private AnimationTimer gameLoop;

    @Override
    public void start(Stage primaryStage) {
        // Initialize game manager
        gameManager = new GameManager();

        // Initialize game view
        gameView = new GameView(gameManager);

        // Set up stage
        primaryStage.setTitle("Arkanoid Game");
        primaryStage.setScene(gameView.getScene());
        primaryStage.setResizable(false);
        primaryStage.show();

        // Start game loop
        startGameLoop();
    }

    private void startGameLoop() {
        final long[] lastUpdate = {System.nanoTime()};

        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                // Calculate delta time in seconds
                double deltaTime = (now - lastUpdate[0]) / 1_000_000_000.0;
                lastUpdate[0] = now;

                // Cap delta time to prevent huge jumps
                deltaTime = Math.min(deltaTime, 0.05);

                // Update game state with delta time
                gameManager.update(deltaTime);

                // Render
                gameView.render(gameManager);
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