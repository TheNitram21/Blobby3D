package de.arnomann.martin.blobby3d.event;

import de.arnomann.martin.blobby3d.math.*;

public class CursorPositionChangedEvent extends Event {

    public final Vector2 newPosition;

    public CursorPositionChangedEvent(Vector2 newPosition) {
        this.newPosition = newPosition;
    }

}
