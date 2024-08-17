package de.arnomann.martin.blobby3d.event;

import de.arnomann.martin.blobby3d.math.*;

public class MouseButtonPressedEvent extends KeyPressedEvent {

    public final Vector2 pos;

    public MouseButtonPressedEvent(int key, Vector2 pos) {
        super(key);
        this.pos = pos;
    }

}
