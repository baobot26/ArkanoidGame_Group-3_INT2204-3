package Arkanoid.level;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public abstract class AbstractLevel implements LevelInterface {
    protected int id;
    protected String name;
    protected boolean completed;
    protected LevelData data;

    /**
     * Constructor for AbstractLevel.
     *
     * @param id   Level ID
     * @param name Level name
     * @param data Level data
     */
    public AbstractLevel(int id, String name, LevelData data) {
        this.id = id;
        this.name = name;
        this.data = data;
        this.completed = false;
    }

    /**
     * Render level elements.
     *
     * @param gc Graphics context
     */
    @Override
    public void render(GraphicsContext gc) {
        gc.setFill(Color.web("#202020"));
        gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        gc.setFill(Color.WHITE);
        gc.fillText("Level: " + name, 20, 40);
    }

    /**
     * Reset level to initial state.
     */
    @Override
    public void reset() {
        completed = false;
    }

    @Override
    public boolean isCompleted() {
        return completed;
    }
}
