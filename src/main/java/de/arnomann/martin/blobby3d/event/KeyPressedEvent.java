package de.arnomann.martin.blobby3d.event;

public class KeyPressedEvent extends Event {

    public final int key;

    public KeyPressedEvent(int key) {
        this.key = key;
    }

}
