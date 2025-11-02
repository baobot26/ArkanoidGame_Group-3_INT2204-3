package Arkanoid.level;

import java.util.ArrayList;
import java.util.List;

/**
 * Manager để quản lý progression giữa các level
 */
public class LevelManager {
    private List<Level> levels;
    private int currentLevelIndex;
    private int totalLevels;
    private int highestUnlockedLevel;

    public LevelManager() {
        this.levels = new ArrayList<>();
        this.currentLevelIndex = 0;
        this.highestUnlockedLevel = Integer.MAX_VALUE; // Unlock tất cả levels
    }

    /**
     * Load tất cả levels từ file
     * @param maxLevels Số level tối đa
     */
    public void loadLevels(int maxLevels) {
        levels.clear();
        this.totalLevels = maxLevels;

        for (int i = 1; i <= maxLevels; i++) {
            LevelData levelData = LevelLoader.loadLevel(i);

            if (levelData != null) {
                Level level = new Level(levelData);
                level.initialize();
                levels.add(level);
            } else {
                // Nếu không load được, tạo level mặc định
                System.out.println("Creating default level " + i);
                LevelData defaultData = LevelLoader.createSampleLevel(i, "Level " + i);
                Level level = new Level(defaultData);
                level.initialize();
                levels.add(level);
            }
        }

        System.out.println("Loaded " + levels.size() + " levels");
    }

    /**
     * Lấy level hiện tại
     */
    public Level getCurrentLevel() {
        if (levels.isEmpty()) return null;
        return levels.get(currentLevelIndex);
    }

    /**
     * Chuyển sang level tiếp theo
     * @return true nếu còn level tiếp theo
     */
    public boolean nextLevel() {
        if (currentLevelIndex < levels.size() - 1) {
            currentLevelIndex++;

            // Unlock level mới nếu chưa unlock
            if (currentLevelIndex + 1 > highestUnlockedLevel) {
                highestUnlockedLevel = currentLevelIndex + 1;
            }

            return true;
        }
        return false; // Hết level
    }

    /**
     * Quay lại level trước
     * @return true nếu thành công
     */
    public boolean previousLevel() {
        if (currentLevelIndex > 0) {
            currentLevelIndex--;
            return true;
        }
        return false;
    }

    /**
     * Chọn level cụ thể (nếu đã unlock)
     * @param levelNumber Level number (1-based)
     * @return true nếu thành công
     */
    public boolean selectLevel(int levelNumber) {
        int index = levelNumber - 1;

        if (index >= 0 && index < levels.size() && levelNumber <= highestUnlockedLevel) {
            currentLevelIndex = index;
            return true;
        }
        return false;
    }

    /**
     * Reset level hiện tại
     */
    public void resetCurrentLevel() {
        if (getCurrentLevel() != null) {
            getCurrentLevel().reset();
        }
    }

    /**
     * Restart từ level 1
     */
    public void restartGame() {
        currentLevelIndex = 0;
        for (Level level : levels) {
            level.reset();
        }
    }

    /**
     * Kiểm tra đã hoàn thành tất cả level chưa
     */
    public boolean isGameComplete() {
        return currentLevelIndex >= levels.size() - 1 &&
                getCurrentLevel() != null &&
                getCurrentLevel().isCompleted();
    }

    /**
     * Lấy số level hiện tại (1-based)
     */
    public int getCurrentLevelNumber() {
        return currentLevelIndex + 1;
    }

    /**
     * Lấy tổng số level
     */
    public int getTotalLevels() {
        return levels.size();
    }

    /**
     * Lấy level cao nhất đã unlock
     */
    public int getHighestUnlockedLevel() {
        return highestUnlockedLevel;
    }

    /**
     * Set level cao nhất đã unlock (dùng cho save/load game)
     */
    public void setHighestUnlockedLevel(int level) {
        this.highestUnlockedLevel = Math.min(level, totalLevels);
    }

    /**
     * Kiểm tra level có unlock không
     */
    public boolean isLevelUnlocked(int levelNumber) {
        return levelNumber <= highestUnlockedLevel;
    }

    /**
     * Unlock tất cả levels (cheat mode hoặc debug)
     */
    public void unlockAllLevels() {
        highestUnlockedLevel = totalLevels;
    }

    /**
     * Lấy danh sách tất cả levels
     */
    public List<Level> getAllLevels() {
        return new ArrayList<>(levels);
    }

    /**
     * Lấy progress (phần trăm hoàn thành)
     */
    public double getProgress() {
        if (totalLevels == 0) return 0.0;
        return (double) (currentLevelIndex + 1) / totalLevels * 100.0;
    }
}