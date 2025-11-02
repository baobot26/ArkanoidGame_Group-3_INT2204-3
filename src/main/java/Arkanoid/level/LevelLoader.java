package Arkanoid.level;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Class để load và lưu level từ JSON files
 */
public class LevelLoader {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final String LEVELS_PATH = "resources/levels/"; // fallback khi chạy trực tiếp từ IDE

    /**
     * Load level từ file JSON (ưu tiên đọc từ resources)
     * @param levelNumber Số thứ tự level (1, 2, 3,...)
     * @return LevelData object hoặc null nếu lỗi
     */
    public static LevelData loadLevel(int levelNumber) {
        String filename = "/levels/level" + levelNumber + ".json"; // trong resources

        try (InputStream is = LevelLoader.class.getResourceAsStream(filename)) {
            if (is != null) {
                // Load từ classpath (resources)
                try (Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
                    LevelData data = gson.fromJson(reader, LevelData.class);
                    System.out.println("✅ Loaded level " + levelNumber + " from resources: " + data.getName());
                    return data;
                }
            } else {
                // fallback: đọc từ thư mục resources/levels ngoài IDE
                String fallback = LEVELS_PATH + "level" + levelNumber + ".json";
                if (Files.exists(Paths.get(fallback))) {
                    String json = new String(Files.readAllBytes(Paths.get(fallback)), StandardCharsets.UTF_8);
                    LevelData data = gson.fromJson(json, LevelData.class);
                    System.out.println("✅ Loaded level " + levelNumber + " from filesystem: " + data.getName());
                    return data;
                } else {
                    System.err.println("❌ Không tìm thấy level file: " + fallback);
                }
            }
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi load level " + levelNumber + ": " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Load tất cả levels có sẵn
     * @param maxLevel Số level tối đa cần load
     * @return Mảng các LevelData (một số phần tử có thể null nếu level không tồn tại)
     */
    public static LevelData[] loadAllLevels(int maxLevel) {
        LevelData[] levels = new LevelData[maxLevel];
        for (int i = 1; i <= maxLevel; i++) {
            levels[i - 1] = loadLevel(i);
        }
        return levels;
    }

    /**
     * Kiểm tra xem level có tồn tại không
     * @param levelNumber Số thứ tự level
     * @return true nếu có file hợp lệ
     */
    public static boolean levelExists(int levelNumber) {
        String pathInResources = "/levels/level" + levelNumber + ".json";
        if (LevelLoader.class.getResource(pathInResources) != null)
            return true;

        return Files.exists(Paths.get(LEVELS_PATH + "level" + levelNumber + ".json"));
    }

    /**
     * Save level ra file JSON (chỉ dùng cho editor hoặc debug)
     * @param levelData LevelData cần save
     * @return true nếu lưu thành công
     */
    public static boolean saveLevel(LevelData levelData) {
        String filename = LEVELS_PATH + "level" + levelData.getLevelNumber() + ".json";

        try {
            // Tạo thư mục nếu chưa tồn tại
            new File(LEVELS_PATH).mkdirs();

            // Ghi file JSON
            try (Writer writer = new OutputStreamWriter(new FileOutputStream(filename), StandardCharsets.UTF_8)) {
                gson.toJson(levelData, writer);
            }

            System.out.println("💾 Saved level " + levelData.getLevelNumber() + " → " + filename);
            return true;
        } catch (IOException e) {
            System.err.println("❌ Lỗi khi lưu level: " + e.getMessage());
            return false;
        }
    }

    /**
     * Tạo level mẫu để test hoặc export JSON
     */
    public static LevelData createSampleLevel(int levelNumber, String name) {
        LevelData levelData = new LevelData();
        levelData.setLevelNumber(levelNumber);
        levelData.setName(name);
        levelData.setBallSpeed(4.5);
        levelData.setLives(3);

        java.util.List<LevelData.BrickData> bricks = new java.util.ArrayList<>();
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 10; col++) {
                LevelData.BrickData brick = new LevelData.BrickData(row, col, "NORMAL", getColorForRow(row));
                bricks.add(brick);
            }
        }
        levelData.setBricks(bricks);
        return levelData;
    }

    /**
     * Helper: màu cho từng hàng
     */
    private static String getColorForRow(int row) {
        String[] colors = {
                "#FF0000", // Red
                "#FFA500", // Orange
                "#FFFF00", // Yellow
                "#00FF00", // Green
                "#00FFFF", // Cyan
                "#0000FF", // Blue
                "#800080", // Purple
                "#FFC0CB"  // Pink
        };
        return colors[row % colors.length];
    }
}
