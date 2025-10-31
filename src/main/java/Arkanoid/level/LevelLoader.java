package Arkanoid.level;

import java.io.FileReader;
import java.io.IOException;
import com.google.gson.Gson;

public class LevelLoader {
    public LevelData loadLevel(String levelPath) {
        try (FileReader reader = new FileReader(levelPath)) {
            Gson gson = new Gson();
            return gson.fromJson(reader, LevelData.class);
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
