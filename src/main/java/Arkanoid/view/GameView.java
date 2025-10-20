package Arkanoid.view;

import Arkanoid.manager.GameManager;
import Arkanoid.renderer.Renderer;
import Arkanoid.util.Constants;
import Arkanoid.util.InputHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;

public class GameView {
    private Scene scene;
    private Canvas canvas;
    private GraphicsContext gc;
    private Renderer renderer;
    private InputHandler inputHandler;

    public GameView(GameManager gameManager) {
        // Create canvas
        canvas = new Canvas(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        gc = canvas.getGraphicsContext2D();

        // Create renderer
        renderer = new Renderer(gc);

        // Create input handler
        inputHandler = new InputHandler(gameManager);

        // Create scene
        StackPane root = new StackPane(canvas);
        scene = new Scene(root, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);

        // Set up input handling
        scene.setOnKeyPressed(inputHandler::handleKeyPressed);
        scene.setOnKeyReleased(inputHandler::handleKeyReleased);
    }

    public void render(GameManager gameManager) {
        renderer.render(gameManager);
    }

    public Scene getScene() {
        return scene;
    }
}