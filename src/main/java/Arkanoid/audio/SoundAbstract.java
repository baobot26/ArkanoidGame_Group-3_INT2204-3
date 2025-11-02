package Arkanoid.audio;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public abstract class SoundAbstract implements SoundInterface{
    protected float volume = 1.0f;
    protected Clip clip;
    /**
     * Loads an audio clip from a filesystem path.
     * Expects a PCM compatible WAV. Stores the opened Clip in {@code clip}.
     * @param soundPath absolute or relative file path to the sound asset
     * @throws RuntimeException if the file can't be read or audio system is unavailable
     */
    public void load(String soundPath) {
        try {
            AudioInputStream audio = AudioSystem.getAudioInputStream(new File(soundPath));
            clip = AudioSystem.getClip();
            clip.open(audio);
            System.out.println("Loaded sound: " + soundPath);
        } catch (UnsupportedAudioFileException | IOException |
                 LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Sets linear volume (0.0 to 1.0). Converts to decibels for MASTER_GAIN.
     * Safe-guards zero using a tiny epsilon to avoid -Inf dB.
     */
    public void setVolume(float volume) {
        this.volume = volume;
        if (clip != null) {
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float dB = (float) (20.0 * Math.log10(volume <= 0.0 ? 0.0001 : volume));
            gainControl.setValue(dB);
        }
    }

}
