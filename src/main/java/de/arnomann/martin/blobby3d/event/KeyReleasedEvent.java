package de.arnomann.martin.blobby3d.event;

public class KeyReleasedEvent extends Event {

    public final int key;

    public KeyReleasedEvent(int key) {
        this.key = key;
    }

}
