package de.arnomann.martin.blobby3d.render;

import de.arnomann.martin.blobby3d.core.Blobby3D;
import de.arnomann.martin.blobby3d.render.texture.ITexture;

import static org.lwjgl.opengl.GL33.*;

public class Renderer {

    private static Shader defaultShader;

    private static boolean initialized;

    private static ITexture texture;
    private static Model model;
    private static int vao;

    private static Camera activeCamera;

    private Renderer() {}

    public static void initialize() {
        if(initialized)
            return;

        if(Blobby3D.getRenderAPI() == RenderAPI.OPENGL) {
            vao = glGenVertexArrays();
            glBindVertexArray(vao);

            glEnableVertexAttribArray(0);
            glEnableVertexAttribArray(1);

            float[] vertices = new float[] { // CUBE
                    -0.5f,  0.5f, -0.5f, // FRONT  TOP     LEFT   0
                     0.5f,  0.5f, -0.5f, // FRONT  TOP     RIGHT  1
                     0.5f, -0.5f, -0.5f, // FRONT  BOTTOM  RIGHT  2
                    -0.5f, -0.5f, -0.5f, // FRONT  BOTTOM  LEFT   3
                    -0.5f,  0.5f,  0.5f, // BACK   TOP     LEFT   4
                     0.5f,  0.5f,  0.5f, // BACK   TOP     RIGHT  5
                     0.5f, -0.5f,  0.5f, // BACK   BOTTOM  RIGHT  6
                    -0.5f, -0.5f,  0.5f  // BACK   BOTTOM  LEFT   7
            };
            float[] textureCoords = new float[] {
                    0f, 1f, // TOP     LEFT
                    1f, 1f, // TOP     RIGHT
                    1f, 0f, // BOTTOM  RIGHT
                    0f, 0f, // BOTTOM  LEFT
                    0f, 1f, // TOP     LEFT
                    1f, 1f, // TOP     RIGHT
                    1f, 0f, // BOTTOM  RIGHT
                    0f, 0f  // BOTTOM  LEFT
            };
            int[] indices = new int[] {
                    0, 1, 2, // FRONT
                    2, 3, 0,
//                    4, 0, 3, // LEFT
//                    3, 7, 4,
//                    5, 4, 7, // BACK
//                    7, 6, 5,
//                    1, 5, 6, // RIGHT
//                    6, 2, 1
            };

            texture = Blobby3D.getTexture("measure");
            model = new Model(vertices, textureCoords, indices);

            float aspectRatio = (float) Blobby3D.getWindow().getWidth() / Blobby3D.getWindow().getHeight();
            activeCamera = new PerspectiveCamera(70, 70 / aspectRatio, 0.01f, 1000f);

            defaultShader = new Shader(Blobby3D.readFile(Blobby3D.SHADERS_PATH + "defaultShader.vert"),
                    Blobby3D.readFile(Blobby3D.SHADERS_PATH + "defaultShader.frag"));
        }

        initialized = true;
    }

    public static void render() {
        if(Blobby3D.getRenderAPI() == RenderAPI.OPENGL) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            texture.bind(0);
            defaultShader.bind();
            defaultShader.setUniform1i("u_Texture", 0);
            defaultShader.setUniformMatrix4("u_ViewProjectionMatrix", activeCamera.getViewProjectionMatrix());
            glBindBuffer(GL_ARRAY_BUFFER, model.getVBO());
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 12, 0);
            glBindBuffer(GL_ARRAY_BUFFER, model.getTBO());
            glVertexAttribPointer(1, 2, GL_FLOAT, false, 8, 0);
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, model.getEBO());
            glDrawElements(GL_TRIANGLES, model.getCount(), GL_UNSIGNED_INT, 0);
        }
    }

    public static Shader getDefaultShader() {
        return defaultShader;
    }

    public static void setActiveCamera(Camera camera) {
        activeCamera = camera;
    }

    public static Camera getActiveCamera() {
        return activeCamera;
    }

}
