package de.arnomann.martin.blobby3d;

import de.arnomann.martin.blobby3d.render.RenderAPI;

public class RunConfigurations {

    public final String title;
    public final int width;
    public final int height;
    public final int maxFramerate;
    public final int multiSampling;
    public final boolean vSyncEnabled;
    public final boolean wireframe;
    public final RenderAPI renderAPI;

    public RunConfigurations(String title, int width, int height, int maxFramerate, int multiSampling,
                             boolean vSyncEnabled, boolean wireframe, RenderAPI renderAPI) {
        this.title = title;
        this.width = width;
        this.height = height;
        this.maxFramerate = maxFramerate;
        this.multiSampling = multiSampling;
        this.vSyncEnabled = vSyncEnabled;
        this.wireframe = wireframe;
        this.renderAPI = renderAPI;
    }

    public static RunConfigurations createDefault(String title, int width, int height) {
        return new RunConfigurations(title, width, height, -1, 0, true, false,
                RenderAPI.OPENGL);
    }

    public static RunConfigurations createDefault(String title, int width, int height, int multiSampling) {
        return new RunConfigurations(title, width, height, -1, multiSampling, true, false,
                RenderAPI.OPENGL);
    }

}
