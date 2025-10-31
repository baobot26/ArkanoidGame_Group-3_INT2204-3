package Arkanoid.audio;

import javax.sound.sampled.Clip;

public class Music extends SoundAbstract {
    private final boolean looping;

    /**
     * Constructor for Music class.
     *
     * @param soundPath Path to the sound file.
     * @param looping   Whether the music should loop continuously.
     */
    public Music(String soundPath, boolean looping) {
        load(soundPath);
        this.looping = looping;
    }

    /**
     * Plays the music. If looping is enabled, it will loop continuously.
     * Otherwise, it will play once from the beginning.
     */
    @Override
    public void play() {
        if (clip != null) {
            clip.stop();
            clip.setFramePosition(0);
            clip.loop(looping ? Clip.LOOP_CONTINUOUSLY : 0);
        }
    }

    /**
     * Stops the music playback.
     */
    @Override
    public void stop() {
        if (clip != null) {
            clip.stop();
        }
    }
}
