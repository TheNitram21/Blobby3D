package de.arnomann.martin.blobby3d.render.texture;

import de.arnomann.martin.blobby3d.core.Blobby3D;
import de.arnomann.martin.blobby3d.render.RenderAPI;
import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryStack;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL33.*;

public class Texture implements ITexture {

    private int id;
    private int width, height;

    public Texture(String filename) {
        TextureData data = TextureData.parseTextureData(filename);
        if(data == null) {
            Blobby3D.getLogger().error("Couldn't load texture \"" + filename + "\": Couldn't load texture data!");
            return;
        }

        if(Blobby3D.getRenderAPI() == RenderAPI.OPENGL) {
            id = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, id);

            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, data.linearFiltering ? GL_LINEAR : GL_NEAREST);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, data.linearFiltering ? GL_LINEAR : GL_NEAREST);

            try(MemoryStack stack = MemoryStack.stackPush()) {
                IntBuffer width = stack.mallocInt(1);
                IntBuffer height = stack.mallocInt(1);

                try {
                    ByteBuffer image = loadImage(filename, width, height);
                    this.width = width.get(0);
                    this.height = height.get(0);

                    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, this.width, this.height, 0, GL_RGBA,
                            GL_UNSIGNED_BYTE, image);
                    glGenerateMipmap(GL_TEXTURE_2D);
                } catch(IOException e) {
                    Blobby3D.getLogger().error("Couldn't load texture at \"" + new File(filename)
                            .getAbsolutePath() + "\":", e);
                    glDeleteTextures(id);
                }
            }
        }
    }

    private static ByteBuffer loadImage(String filename, IntBuffer widthBuffer, IntBuffer heightBuffer)
            throws IOException {
        BufferedImage image = ImageIO.read(new File(filename));
        int width = image.getWidth();
        int height = image.getHeight();
        widthBuffer.put(0, width);
        heightBuffer.put(0, height);

        int[] pixels = image.getRGB(0, 0, width, height, null, 0, width);
        ByteBuffer pixelsBuffer = BufferUtils.createByteBuffer(width * height * 4);

        for(int i = 0; i < width; i++) {
            for(int j = 0; j < height; j++) {
                int pixel = pixels[i * height + j];
                pixelsBuffer.put((byte) ((pixel >> 16) & 0xFF));  // R
                pixelsBuffer.put((byte) ((pixel >> 8) & 0xFF));   // G
                pixelsBuffer.put((byte) (pixel & 0xFF));          // B
                pixelsBuffer.put((byte) ((pixel >> 24) & 0xFF));  // A
            }
        }

        pixelsBuffer.flip();
        return pixelsBuffer;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void bind(int sampler) {
        glActiveTexture(GL_TEXTURE0 + sampler);
        glBindTexture(GL_TEXTURE_2D, id);
    }

}
