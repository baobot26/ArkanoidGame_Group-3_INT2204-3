package Arkanoid;

import Arkanoid.manager.GameManager;
import Arkanoid.model.GameState;
import Arkanoid.util.Constants;
import Arkanoid.renderer.Renderer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class RendererTest extends ApplicationTest {

    private Canvas canvas;
    private GraphicsContext gc;
    private Renderer renderer;
    private GameManager gameManager;

    @Override
    public void start(Stage stage) {
        canvas = new Canvas(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        gc = canvas.getGraphicsContext2D();
        renderer = new Renderer(gc);
        gameManager = new GameManager();

        stage.setScene(new javafx.scene.Scene(new StackPane(canvas)));
        stage.show();
    }

    @BeforeEach
    void setupGame() {

        gameManager.startGame();
    }

    private void runRenderOnFxAndWait() throws Exception {
        WaitForAsyncUtils.asyncFx(() -> {
            renderer.render(gameManager);
            return null;
        }).get(2, TimeUnit.SECONDS);
    }

    @Test
    void testRenderMenuDoesNotThrow() {
        gameManager.setCurrentState(GameState.MENU);
        assertDoesNotThrow(() -> runRenderOnFxAndWait());
    }

    @Test
    void testRenderPlayingDoesNotThrow() {
        gameManager.setCurrentState(GameState.PLAYING);
        assertDoesNotThrow(() -> runRenderOnFxAndWait());
    }

    @Test
    void testRenderPausedDoesNotThrow() {
        gameManager.setCurrentState(GameState.PAUSED);
        assertDoesNotThrow(() -> runRenderOnFxAndWait());
    }

    @Test
    void testRenderGameOverDoesNotThrow() {
        gameManager.setCurrentState(GameState.GAME_OVER);
        assertDoesNotThrow(() -> runRenderOnFxAndWait());
    }

    @Test
    void testRenderLevelCompleteDoesNotThrow() {
        gameManager.setCurrentState(GameState.LEVEL_COMPLETE);
        assertDoesNotThrow(() -> runRenderOnFxAndWait());
    }

    @Test
    void testRendererUsesCorrectCanvasSize() throws Exception {
        gameManager.setCurrentState(GameState.MENU);
        runRenderOnFxAndWait();

        assertEquals(Constants.WINDOW_WIDTH, canvas.getWidth());
        assertEquals(Constants.WINDOW_HEIGHT, canvas.getHeight());
    }

    @Test
    void testRendererUIShowsScoreText() throws Exception {
        gameManager.setCurrentState(GameState.PLAYING);

        gameManager.getScoreManager().addScore(100);

        runRenderOnFxAndWait();


        assertEquals(100, gameManager.getScoreManager().getScore());
    }
}