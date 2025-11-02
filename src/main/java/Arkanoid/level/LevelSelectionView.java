package Arkanoid.level;

import Arkanoid.util.Constants;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * UI View để chọn level
 */
public class LevelSelectionView {
    private final Stage stage;
    private final LevelManager levelManager;
    private LevelSelectionCallback callback;

    // UI Components
    private VBox mainContainer;
    private GridPane levelGrid;
    private Label titleLabel;
    private Button backButton;

    // ✅ Giữ Scene để có thể quay lại dễ dàng
    private Scene scene;

    /**
     * Interface callback khi chọn level
     */
    public interface LevelSelectionCallback {
        void onLevelSelected(int levelNumber);
        void onBack();
    }

    public LevelSelectionView(Stage stage, LevelManager levelManager) {
        this.stage = stage;
        this.levelManager = levelManager;
        initializeUI();
    }

    /**
     * Khởi tạo UI
     */
    private void initializeUI() {
        mainContainer = new VBox(20);
        mainContainer.setAlignment(Pos.TOP_CENTER);
        mainContainer.setPadding(new Insets(40));
        mainContainer.setStyle("-fx-background-color: #1a1a2e;");

        // Title
        titleLabel = new Label("SELECT LEVEL");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 48));
        titleLabel.setTextFill(Color.WHITE);

        // Level Grid
        levelGrid = new GridPane();
        levelGrid.setHgap(20);
        levelGrid.setVgap(20);
        levelGrid.setAlignment(Pos.CENTER);

        createLevelButtons();

        // Back Button
        backButton = new Button("BACK TO MENU");
        backButton.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        backButton.setPrefSize(250, 50);
        styleButton(backButton, "#e74c3c");

        backButton.setOnAction(e -> {
            if (callback != null) {
                callback.onBack();
            }
        });

        mainContainer.getChildren().addAll(titleLabel, levelGrid, backButton);

        // ✅ tạo scene ngay khi khởi tạo
        scene = new Scene(mainContainer, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);

        // ✅ xử lý phím ESC để quay về MENU luôn tại đây
        scene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case ESCAPE:
                    if (callback != null) {
                        callback.onBack(); // quay về menu khi bấm ESC
                    }
                    break;
                default:
                    break;
            }
        });
    }

    /**
     * Tạo các button cho từng level
     */
    private void createLevelButtons() {
        levelGrid.getChildren().clear();

        int totalLevels = levelManager.getTotalLevels();
        int cols = 5; // 5 level mỗi hàng

        for (int i = 1; i <= totalLevels; i++) {
            Button levelButton = createLevelButton(i);
            int row = (i - 1) / cols;
            int col = (i - 1) % cols;
            levelGrid.add(levelButton, col, row);
        }
    }

    private Button createLevelButton(int levelNumber) {
        Button button = new Button(String.valueOf(levelNumber));
        button.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        button.setPrefSize(100, 100);

        boolean isUnlocked = levelManager.isLevelUnlocked(levelNumber);

        if (isUnlocked) {
            styleButton(button, "#3498db");
            button.setOnAction(e -> {
                if (callback != null) {
                    callback.onLevelSelected(levelNumber);
                }
            });

            if (levelNumber == levelManager.getCurrentLevelNumber()) {
                styleButton(button, "#2ecc71");
            }
        } else {
            button.setText("🔒");
            styleButton(button, "#7f8c8d");
            button.setDisable(true);
        }

        return button;
    }

    private void styleButton(Button button, String color) {
        button.setStyle(
                "-fx-background-color: " + color + ";" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 10;" +
                        "-fx-border-radius: 10;" +
                        "-fx-border-color: white;" +
                        "-fx-border-width: 2;" +
                        "-fx-cursor: hand;"
        );

        button.setOnMouseEntered(e -> {
            if (!button.isDisabled()) {
                button.setStyle(
                        "-fx-background-color: derive(" + color + ", 20%);" +
                                "-fx-text-fill: white;" +
                                "-fx-background-radius: 10;" +
                                "-fx-border-radius: 10;" +
                                "-fx-border-color: white;" +
                                "-fx-border-width: 3;" +
                                "-fx-cursor: hand;" +
                                "-fx-scale-x: 1.05;" +
                                "-fx-scale-y: 1.05;"
                );
            }
        });

        button.setOnMouseExited(e -> {
            if (!button.isDisabled()) {
                styleButton(button, color); // Reset
            }
        });
    }

    /**
     * Hiển thị level selection screen
     */
    public void show() {
        createLevelButtons(); // Refresh buttons
        stage.setScene(scene);
        stage.setTitle("Arkanoid - Level Selection");
    }

    /**
     * ✅ Lấy Scene cho nơi khác dùng (ví dụ Main hoặc InputHandler)
     */
    public Scene getScene() {
        return scene;
    }

    public void refresh() {
        createLevelButtons();
    }

    public void setCallback(LevelSelectionCallback callback) {
        this.callback = callback;
    }

    public VBox getMainContainer() {
        return mainContainer;
    }
}
