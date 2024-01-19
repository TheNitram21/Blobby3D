package de.arnomann.martin.blobby3d.event;

import org.joml.Vector2f;

public class CursorPositionChangedEvent extends Event {

    public final Vector2f newPosition;

    public CursorPositionChangedEvent(Vector2f newPosition) {
        this.newPosition = newPosition;
    }

}
