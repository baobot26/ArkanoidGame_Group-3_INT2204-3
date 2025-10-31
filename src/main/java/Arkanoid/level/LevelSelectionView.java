package Arkanoid.level;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LevelSelectionView {
    private final LevelManager manager;
    private final Stage stage;
    private Scene scene;

    /**
     * Constructor for LevelSelectionView.
     *
     * @param stage   Primary stage
     * @param manager LevelManager instance
     */
    public LevelSelectionView(Stage stage, LevelManager manager) {
        this.stage = stage;
        this.manager = manager;
        initUI();
    }

    /**
     * Initialize the UI components.
     */
    private void initUI() {
        VBox root = new VBox(15);
        root.setAlignment(Pos.CENTER);

        for (LevelInterface level : manager.getLevels().values()) {
            Button btn = new Button(((AbstractLevel) level).name);
            btn.setOnAction(e -> {
                level.load();
                level.start();
                System.out.println("Selected: " + ((AbstractLevel) level).name);
                // Move to game view here
            });
            root.getChildren().add(btn);
        }

        scene = new Scene(root, 400, 400);
    }

    public Scene getScene() {
        return scene;
    }
}
