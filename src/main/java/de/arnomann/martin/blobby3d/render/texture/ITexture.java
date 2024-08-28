package de.arnomann.martin.blobby3d.render.texture;

import de.arnomann.martin.blobby3d.core.Blobby3D;
import de.arnomann.martin.blobby3d.render.RenderAPI;

import static org.lwjgl.opengl.GL46.*;

public interface ITexture {

    int getWidth();
    int getHeight();
    int getId();
    TextureData getData();
    void bind(int sampler);
    default void unbind() {
        if(Blobby3D.getRenderAPI() == RenderAPI.OPENGL)
            glBindTexture(GL_TEXTURE_2D, 0);
    }

}
