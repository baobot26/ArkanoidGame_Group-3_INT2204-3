package Arkanoid.level;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Level extends AbstractLevel {
    /**
     * Constructor for Level.
     *
     * @param id   Level ID
     * @param name Level name
     * @param data Level data
     */
    public Level(int id, String name, LevelData data) {
        super(id, name, data);
    }

    /**
     * Load level resources and initialize level state.
     */
    @Override
    public void load() {
        System.out.println("Loading level: " + name);
    }

    /**
     * Start the level
     */
    @Override
    public void start() {
        System.out.println("Starting level: " + name);
    }

    /**
     * Update level state.
     *
     * @param dt Time delta since last update
     */
    @Override
    public void update(double dt) {

    }

    /**
     * Render level elements.
     *
     * @param gc Graphics context
     */
    @Override
    public void render(GraphicsContext gc) {
        super.render(gc);
        // Additional rendering logic can be added here
        if (data != null) {
            gc.setFill(Color.LIGHTBLUE);
            gc.fillText("Map: " + data.getBackground(), 20, 70);

            // Render bricks
        }
    }
}

