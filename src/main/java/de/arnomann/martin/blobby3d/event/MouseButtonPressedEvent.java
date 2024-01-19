package de.arnomann.martin.blobby3d.event;

import org.joml.Vector2f;

public class MouseButtonPressedEvent extends KeyPressedEvent {

    public final Vector2f pos;

    public MouseButtonPressedEvent(int key, Vector2f pos) {
        super(key);
        this.pos = pos;
    }

}
