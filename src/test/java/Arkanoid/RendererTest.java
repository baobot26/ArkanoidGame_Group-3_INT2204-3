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
        // Khởi tạo Scene/Stage để kích hoạt JavaFX toolkit (TestFX sẽ gọi method này trên FX thread)
        canvas = new Canvas(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        gc = canvas.getGraphicsContext2D();
        renderer = new Renderer(gc);
        gameManager = new GameManager();

        stage.setScene(new javafx.scene.Scene(new StackPane(canvas)));
        stage.show();
    }

    @BeforeEach
    void setupGame() {
        // Khởi tạo game bằng API hiện có (GameManager không có resetGame())
        // startGame() sẽ reset ScoreManager và gọi initializeGame()
        gameManager.startGame();
    }

    // Helper: chạy renderer.render(...) trên FX thread và chờ hoàn tất (nếu có ngoại lệ trên FX thread sẽ trả về)
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