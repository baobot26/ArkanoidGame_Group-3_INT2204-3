package Arkanoid.view;

import Arkanoid.audio.AudioSetting;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/** Simple modal Settings window: allows changing music/effects/ambient volumes and saves to settings.json */
public class SettingsView {
    public static void showSettings() {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Settings");

        AudioSetting settings = AudioSetting.getInstance();

        Slider music = new Slider(0, 1, settings.getMusicVolume());
        music.setShowTickLabels(true);
        music.setShowTickMarks(true);
        music.setMajorTickUnit(0.25);

        Slider effects = new Slider(0, 1, settings.getEffectVolume());
        effects.setShowTickLabels(true);
        effects.setShowTickMarks(true);
        effects.setMajorTickUnit(0.25);

        Slider ambient = new Slider(0, 1, settings.getAmbientVolume());
        ambient.setShowTickLabels(true);
        ambient.setShowTickMarks(true);
        ambient.setMajorTickUnit(0.25);

        Button apply = new Button("Apply");
        Button save = new Button("Save");
        Button close = new Button("Close");

        apply.setOnAction(e -> {
            settings.setMusicVolume((float) music.getValue());
            settings.setEffectVolume((float) effects.getValue());
            settings.setAmbientVolume((float) ambient.getValue());
            settings.apply();
        });
        save.setOnAction(e -> {
            settings.setMusicVolume((float) music.getValue());
            settings.setEffectVolume((float) effects.getValue());
            settings.setAmbientVolume((float) ambient.getValue());
            settings.apply();
            settings.saveSettings();
        });
        close.setOnAction(e -> dialog.close());

        VBox root = new VBox(10,
                new Label("Background Volume"), music,
                new Label("Effects Volume"), effects,
                new Label("Ambient Volume"), ambient,
                apply, save, close);
        root.setPadding(new Insets(15));

        dialog.setScene(new Scene(root, 360, 300));
        dialog.showAndWait();
    }
}
