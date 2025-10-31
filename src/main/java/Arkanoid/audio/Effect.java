package Arkanoid.audio;

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
            clip.stop();
            clip.setFramePosition(0);
            clip.start();
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
