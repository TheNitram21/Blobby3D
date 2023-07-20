package de.arnomann.martin.blobby3d.event;

public class WindowResizedEvent extends Event {

    public final int oldWidth;
    public final int oldHeight;
    public final int newWidth;
    public final int newHeight;

    public WindowResizedEvent(int oldWidth, int oldHeight, int newWidth, int newHeight) {
        this.oldWidth = oldWidth;
        this.oldHeight = oldHeight;
        this.newWidth = newWidth;
        this.newHeight = newHeight;
    }

}
