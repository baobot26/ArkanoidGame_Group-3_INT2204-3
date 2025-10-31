package Arkanoid.audio;

public class Ambient extends Music{
    /**
     * Constructor for Ambient class.
     *
     * @param soundPath Path to the sound file.
     */
    public Ambient(String soundPath) {
        super(soundPath, true);
        setVolume(0.2f);
    }
}
