package de.arnomann.martin.blobby3d.render;

import de.arnomann.martin.blobby3d.core.Blobby3D;
import de.arnomann.martin.blobby3d.entity.Entity;
import de.arnomann.martin.blobby3d.entity.PointLight;
import de.arnomann.martin.blobby3d.level.Block;
import de.arnomann.martin.blobby3d.render.texture.ITexture;
import de.arnomann.martin.blobby3d.math.*;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL46.*;

public class Renderer {

    private static Shader defaultShader, unlitShader;

    private static boolean initialized;

    private static int vao;
    private static Camera camera;
    private static Shader shader;

    private static int quadVBO, quadTBO;

    private static List<PointLight> pointLights = new ArrayList<>();

    private Renderer() {}

    public static void initialize() {
        if(initialized)
            return;

        if(Blobby3D.getRenderAPI() == RenderAPI.OPENGL) {
            vao = glGenVertexArrays();
            glBindVertexArray(vao);

            quadVBO = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, quadVBO);
            glBufferData(GL_ARRAY_BUFFER, new float[] {
//                    -0.5f,  0.5f,
//                     0.5f, -0.5f,
//                    -0.5f, -0.5f,
//                    -0.5f,  0.5f,
//                     0.5f,  0.5f,
//                     0.5f, -0.5f
                    0f, 0f, 0f,
                    1f, 0f, 0f,
                    0f, 1f, 0f,
                    1f, 0f, 0f,
                    1f, 1f, 0f,
                    0f, 1f, 0f
            }, GL_STATIC_DRAW);
            quadTBO = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, quadTBO);
            glBufferData(GL_ARRAY_BUFFER, new float[] {
                    0f, 0f,
                    1f, 0f,
                    0f, 1f,
                    1f, 0f,
                    1f, 1f,
                    0f, 1f
            }, GL_STATIC_DRAW);
            glBindBuffer(GL_ARRAY_BUFFER, 0);

            glEnableVertexAttribArray(0);
            glEnableVertexAttribArray(1);
            glEnableVertexAttribArray(2);

            float aspectRatio = (float) Blobby3D.getWindow().getWidth() / Blobby3D.getWindow().getHeight();
            camera = new PerspectiveCamera(70, aspectRatio, 0.01f, 1000f);

            defaultShader = Shader.createFromName("litDefault");
            unlitShader = Shader.createFromName("unlitDefault");
            shader = defaultShader;
        }

        initialized = true;
    }

    public static void render() {
        pointLights.clear();
        for(Entity entity : Blobby3D.getLevel().getEntities()) {
            if(entity instanceof PointLight)
                pointLights.add((PointLight) entity);
        }

        if(Blobby3D.getRenderAPI() == RenderAPI.OPENGL) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            Blobby3D.getLevel().getBlocks().forEach(Renderer::renderBlock);
            Blobby3D.getLevel().getEntities().forEach(Renderer::renderEntity);
        }
    }

    public static void renderBlock(Block block) {
        if(block.texture == null)
            return;

        shader.bind();
        block.texture.bind(0);
        shader.setUniform1i("u_Texture", 0);

        shader.setUniformVector3("u_AmbientLightColor", Blobby3D.getLevel().getAmbientLightColor());

        for(int i = 0; i < pointLights.size(); i++)
            shader.setUniformPointLight("u_PointLights[" + i + "]", pointLights.get(i));
        shader.setUniform1i("u_PointLightCount", pointLights.size());

        shader.setUniformVector3("u_CameraPosition", camera.getPosition());

        shader.setUniformMatrix3("u_NormalMatrix", new Matrix3(block.getModelMatrix()).inverse().transpose());
        shader.setUniformMatrix4("u_ModelMatrix", block.getModelMatrix());
        shader.setUniformMatrix4("u_ModelViewProjectionMatrix", camera.getViewProjectionMatrix().mul(block.getModelMatrix()));

        glBindBuffer(GL_ARRAY_BUFFER, block.getVBO());
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 12, 0);
        glBindBuffer(GL_ARRAY_BUFFER, block.getTBO());
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 8, 0);
        glBindBuffer(GL_ARRAY_BUFFER, block.getNBO());
        glVertexAttribPointer(2, 3, GL_FLOAT, false, 12, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, block.getEBO());
        glDrawElements(GL_TRIANGLES, block.getFaceCount() * 6, GL_UNSIGNED_INT, 0);
    }

    public static void renderEntity(Entity entity) {
        if(entity.getMesh() == null)
            return;

        entity.getShader().bind();
        entity.getTexture().bind(0);
        entity.getShader().setUniform1i("u_Texture", 0);

        entity.getShader().setUniformVector3("u_AmbientLightColor", Blobby3D.getLevel().getAmbientLightColor());

        for(int i = 0; i < pointLights.size(); i++)
            entity.getShader().setUniformPointLight("u_PointLights[" + i + "]", pointLights.get(i));
        entity.getShader().setUniform1i("u_PointLightCount", pointLights.size());

        entity.getShader().setUniformVector3("u_CameraPosition", camera.getPosition());

        entity.getShader().setUniformMatrix3("u_NormalMatrix", new Matrix3(entity.getModelMatrix()).inverse()
                .transpose());
        entity.getShader().setUniformMatrix4("u_ModelMatrix", entity.getModelMatrix());
        entity.getShader().setUniformMatrix4("u_ModelViewProjectionMatrix", camera.getViewProjectionMatrix().mul(entity.getModelMatrix()));

        glBindBuffer(GL_ARRAY_BUFFER, entity.getMesh().getVBO());
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 12, 0);
        glBindBuffer(GL_ARRAY_BUFFER, entity.getMesh().getTBO());
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 8, 0);
        glBindBuffer(GL_ARRAY_BUFFER, entity.getMesh().getNBO());
        glVertexAttribPointer(2, 3, GL_FLOAT, false, 12, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, entity.getMesh().getEBO());
        glDrawElements(GL_TRIANGLES, entity.getMesh().getCount(), GL_UNSIGNED_INT, 0);
    }

    public static void renderSprite(ITexture texture, Vector3 position) {
        if(texture == null)
            return;

        unlitShader.bind();
        texture.bind(0);
        unlitShader.setUniform1i("u_Texture", 0);

        unlitShader.setUniformMatrix4("u_ModelViewProjectionMatrix", new Matrix4(
                camera.getViewProjectionMatrix()).mul(new Matrix4().translate(position).rotate(camera.getRotation())));

        glBindBuffer(GL_ARRAY_BUFFER, quadVBO);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 12, 0);
        glBindBuffer(GL_ARRAY_BUFFER, quadTBO);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 8, 0);
        glDrawArrays(GL_TRIANGLES, 0, 6);
    }

    public static Shader getDefaultShader() {
        return defaultShader;
    }

    public static void setCamera(Camera camera) {
        Renderer.camera = camera;
    }

    public static Camera getCamera() {
        return camera;
    }

}
