package Arkanoid.level;

import java.util.List;

public class LevelData {
    private final String background;
    private final List<double[]> bricks;

    /**
     * Constructor for LevelData.
     *
     * @param background Background image path
     * @param bricks     List of brick types
     */
    public LevelData(String background, List<double[]> bricks) {
        this.background = background;
        this.bricks = bricks;
    }

    /**
     * Get the background image path.
     *
     * @return Background image path
     */
    public String getBackground() {
        return background;
    }

    /**
     * Get the list of brick types.
     *
     * @return List of brick types
     */
    public List<double[]> getBricks() {
        return bricks;
    }
}
