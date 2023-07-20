package de.arnomann.martin.blobby3d;

import de.arnomann.martin.blobby3d.render.RenderAPI;

public class RunConfigurations {

    public final String title;
    public final int width;
    public final int height;
    public final int maxFramerate;
    public final boolean vSyncEnabled;
    public final RenderAPI renderAPI;

    public RunConfigurations(String title, int width, int height, int maxFramerate, boolean vSyncEnabled,
                             RenderAPI renderAPI) {
        this.title = title;
        this.width = width;
        this.height = height;
        this.maxFramerate = maxFramerate;
        this.vSyncEnabled = vSyncEnabled;
        this.renderAPI = renderAPI;
    }

    public static RunConfigurations createDefault(String title, int width, int height) {
        return new RunConfigurations(title, width, height, -1, true, RenderAPI.OPENGL);
    }

}
