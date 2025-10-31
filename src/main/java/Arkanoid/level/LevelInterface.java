package Arkanoid.level;

import javafx.scene.canvas.GraphicsContext;

public interface LevelInterface {
    /**
     * Load level resources and initialize level state.
     */
    void load();

    /**
     * Start the level
     */
    void start();

    /**
     * Update level state.
     *
     * @param dt Time delta since last update
     */
    void update(double dt);

    /**
     * Render level elements.
     *
     * @param gc Graphics context
     */
    void render(GraphicsContext gc);

    /**
     * Check if the level is completed.
     *
     * @return true if completed, false otherwise
     */
    boolean isCompleted();

    /**
     * Reset level to initial state.
     */
    void reset();
}
