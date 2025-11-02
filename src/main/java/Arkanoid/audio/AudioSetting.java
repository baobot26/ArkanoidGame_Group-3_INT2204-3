package Arkanoid.audio;

import java.util.Set;

public class AudioSetting {
    private float masterVolume = 1.0f;
    private float musicVolume = 1.0f;
    private float effectVolume = 1.0f;
    private boolean mute = false;

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
        SoundManager soundManager = SoundManager.getInstance();
        Set<String> names = soundManager.getSoundNames();
        for (String name : names) {
            if (name.toLowerCase().contains("music")) {
                soundManager.setVolume(name, getMasterVolume() * musicVolume);
            } else if (name.toLowerCase().contains("effect")) {
                soundManager.setVolume(name, getMasterVolume() * effectVolume);
            } else {
                soundManager.setVolume(name, getMasterVolume());
            }
        }
    }

    /**
     * Saves the current audio settings.
     */
    public void saveSettings() {
        // Placeholder for saving settings to a file or database
    }

    /**
     * Loads audio settings.
     */
    public void loadSettings() {
        // Placeholder for loading settings from a file or database
        apply();
    }

    /**
     * Resets audio settings to default values.
     */
    public void resetSettings() {
        this.masterVolume = 1.0f;
        this.mute = false;
        apply();
    }
}
