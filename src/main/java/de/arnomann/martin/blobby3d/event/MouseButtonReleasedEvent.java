package de.arnomann.martin.blobby3d.event;

import de.arnomann.martin.blobby3d.math.*;

public class MouseButtonReleasedEvent extends KeyReleasedEvent {

    public final Vector2 pos;

    public MouseButtonReleasedEvent(int key, Vector2 pos) {
        super(key);
        this.pos = pos;
    }

}
