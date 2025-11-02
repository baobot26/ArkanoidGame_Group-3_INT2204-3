package Arkanoid.audio;

import java.awt.Toolkit;

public class Effect extends SoundAbstract {
    /**
     * Constructor for Effect class.
     *
     * @param soundPath Path to the sound file.
     */
    public Effect(String soundPath) {
        load(soundPath);
    }

    /**
     * Plays the sound effect from the beginning.
     */
    @Override
    public void play() {
        if (clip != null) {
            // AudioClip play() already starts from the beginning by default
            clip.stop();
            clip.setCycleCount(1);
            clip.play();
        } else {
            // Fallback: system beep if no valid clip (e.g., zero-byte WAVs)
            try { Toolkit.getDefaultToolkit().beep(); } catch (Throwable ignored) {}
        }
    }

    /**
     * Stops the sound effect playback.
     */
    @Override
    public void stop() {
        if (clip != null) {
            clip.stop();
        }
    }
}
