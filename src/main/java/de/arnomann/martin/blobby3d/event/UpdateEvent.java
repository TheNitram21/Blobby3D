package de.arnomann.martin.blobby3d.event;

public class UpdateEvent extends Event {

    public final double deltaTime;
    public final double time;
    public final double fps;

    public UpdateEvent(double deltaTime, double time) {
        this.deltaTime = deltaTime;
        this.time = time;
        this.fps = 1 / deltaTime;
    }

}
