package Arkanoid.audio;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class AudioSetting {
    private static volatile AudioSetting instance;

    private float masterVolume = 1.0f;
    private float musicVolume = 0.6f;   // background music (MediaPlayers)
    private float effectVolume = 1.0f;  // effect_*
    private float ambientVolume = 0.12f; // ambient_bg
    private boolean mute = false;

    private static final String SETTINGS_FILE = "settings.json";
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static AudioSetting getInstance() {
        if (instance == null) {
            synchronized (AudioSetting.class) {
                if (instance == null) instance = new AudioSetting();
            }
        }
        return instance;
    }

    /**
     * @return the effective master volume (0.0-1.0), returns 0.0 if muted.
     */
    public float getMasterVolume() {
        // If muted, return 0.0 regardless of masterVolume
        return mute ? 0.0f : masterVolume;
    }

    /**
     * Sets the master volume, clamped to 0.0-1.0.
     */
    public void setMasterVolume(float volume) {
        // Clamp volume between 0.0 and 1.0
        this.masterVolume = Math.max(0.0f, Math.min(1.0f, volume));
    }

    /**
     * @return true if audio is muted.
     */
    public boolean isMute() {
        return mute;
    }

    /**
     * Enables or disables mute.
     */
    public void setMute(boolean mute) {
        this.mute = mute;
    }

    /**
     * Applies the current audio settings to all sounds managed by SoundManager.
     */
    public void apply() {
        SoundManager sm = SoundManager.getInstance();
        float master = getMasterVolume();

        // Background (MediaPlayers)
        sm.setBackgroundVolume(master * musicVolume);

        // Ambient
        sm.setAmbientVolume(master * ambientVolume);

        // Effects
        sm.setEffectsVolume(master * effectVolume);
    }

    /**
     * Saves the current audio settings.
     */
    public void saveSettings() {
        try (Writer w = new OutputStreamWriter(new FileOutputStream(SETTINGS_FILE), StandardCharsets.UTF_8)) {
            GSON.toJson(this, w);
        } catch (IOException e) {
            System.err.println("Failed to save settings: " + e.getMessage());
        }
    }

    /**
     * Loads audio settings.
     */
    public void loadSettings() {
        File f = new File(SETTINGS_FILE);
        if (!f.exists()) {
            apply();
            return;
        }
        try (Reader r = new InputStreamReader(new FileInputStream(f), StandardCharsets.UTF_8)) {
            AudioSetting loaded = GSON.fromJson(r, AudioSetting.class);
            if (loaded != null) {
                this.masterVolume = loaded.masterVolume;
                this.musicVolume = loaded.musicVolume;
                this.effectVolume = loaded.effectVolume;
                this.ambientVolume = loaded.ambientVolume;
                this.mute = loaded.mute;
            }
        } catch (IOException e) {
            System.err.println("Failed to load settings: " + e.getMessage());
        }
        apply();
    }

    /**
     * Resets audio settings to default values.
     */
    public void resetSettings() {
        this.masterVolume = 1.0f;
        this.musicVolume = 0.6f;
        this.effectVolume = 1.0f;
        this.ambientVolume = 0.12f;
        this.mute = false;
        apply();
    }

    // Getters/setters for component volumes
    public float getMusicVolume() { return musicVolume; }
    public void setMusicVolume(float v) { musicVolume = clamp01(v); }
    public float getEffectVolume() { return effectVolume; }
    public void setEffectVolume(float v) { effectVolume = clamp01(v); }
    public float getAmbientVolume() { return ambientVolume; }
    public void setAmbientVolume(float v) { ambientVolume = clamp01(v); }

    private float clamp01(float v) { return Math.max(0f, Math.min(1f, v)); }
}
