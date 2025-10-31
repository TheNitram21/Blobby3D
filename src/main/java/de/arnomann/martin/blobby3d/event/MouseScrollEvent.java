package de.arnomann.martin.blobby3d.event;

public class MouseScrollEvent extends Event {

    public final double x;
    public final double y;

    public MouseScrollEvent(double x, double y) {
        this.x = x;
        this.y = y;
    }

}
