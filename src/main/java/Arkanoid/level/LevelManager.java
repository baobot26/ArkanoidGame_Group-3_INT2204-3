package Arkanoid.level;

import java.util.LinkedHashMap;
import java.util.Map;

public class LevelManager {
    private final Map<Integer, LevelInterface> levels = new LinkedHashMap<>();
    private final LevelLoader levelLoader = new LevelLoader();

    /**
     * Constructor for LevelManager.
     * Loads levels from predefined paths.
     */
    public LevelManager() {
        loadLevels();
    }

    /**
     * Load levels from predefined paths.
     */
    private void loadLevels() {
        String[] levelPaths = {
                "resources/levels/level1.json",
                "resources/levels/level2.json",
        };

        for (int i = 0; i < levelPaths.length; i++) {
            LevelData data = levelLoader.loadLevel(levelPaths[i]);
            if (data != null) {
                LevelInterface level = new Level(i + 1, "Level " + (i + 1), data);
                levels.put(i + 1, level);
            }
        }
    }

    /**
     * Get level by ID.
     *
     * @param id Level ID
     * @return LevelInterface instance
     */
    public LevelInterface getLevel(int id) {
        return levels.get(id);
    }

    public Map<Integer, LevelInterface> getLevels() {
        return levels;
    }
}
