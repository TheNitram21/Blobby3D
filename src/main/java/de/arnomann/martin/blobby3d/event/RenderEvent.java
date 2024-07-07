package de.arnomann.martin.blobby3d.event;

public class RenderEvent extends Event {

    public final double time;

    public RenderEvent(double time) {
        this.time = time;
    }

}
