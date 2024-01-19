package de.arnomann.martin.blobby3d.event;

import org.joml.Vector2f;

public class MouseButtonReleasedEvent extends KeyReleasedEvent {

    public final Vector2f pos;

    public MouseButtonReleasedEvent(int key, Vector2f pos) {
        super(key);
        this.pos = pos;
    }

}
