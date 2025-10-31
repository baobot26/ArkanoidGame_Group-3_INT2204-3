package Arkanoid.audio;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SoundManager {
    private static SoundManager instance;
    private final Map<String, SoundInterface> sounds = new HashMap<>();

    private SoundManager() {
    }

    /**
     * Retrieves the singleton instance of SoundManager.
     *
     * @return The SoundManager instance.
     */
    public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }

    /**
     * Adds a sound to the sound manager.
     *
     * @param name  The name of the sound.
     * @param sound The sound object implementing SoundInterface.
     */
    public void addSound(String name, SoundInterface sound) {
        sounds.put(name, sound);
    }

    /**
     * Plays a sound by its name.
     *
     * @param name The name of the sound to play.
     */
    public void playSound(String name) {
        SoundInterface sound = sounds.get(name);
        if (sound != null) {
            sound.play();
        } else {
            System.err.println("Sound not found: " + name);
        }
    }

    /**
     * Stops a sound by its name.
     *
     * @param name The name of the sound to stop.
     */
    public void stopSound(String name) {
        SoundInterface sound = sounds.get(name);
        if (sound != null) {
            sound.stop();
        } else {
            System.err.println("Sound not found: " + name);
        }
    }

    /**
     * Sets the volume for a specific sound.
     *
     * @param name   The name of the sound.
     * @param volume The volume level (0.0 to 1.0).
     */
    public void setVolume(String name, float volume) {
        SoundInterface sound = sounds.get(name);
        if (sound instanceof SoundAbstract) {
            ((SoundAbstract) sound).setVolume(volume);
        } else {
            System.err.println("Sound not found or invalid type: " + name);
        }
    }

    /**
     * Retrieves the set of sound names managed by the SoundManager.
     *
     * @return A set of sound names.
     */
    public Set<String> getSoundNames() {
        return sounds.keySet();
    }

    /**
     * Clears all sounds from the sound manager.
     */
    public void clearSounds() {
        for (SoundInterface sound : sounds.values()) {
            sound.stop();
        }
        sounds.clear();
    }
}
