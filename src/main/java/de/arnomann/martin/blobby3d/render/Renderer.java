package de.arnomann.martin.blobby3d.render;

import de.arnomann.martin.blobby3d.core.Blobby3D;
import de.arnomann.martin.blobby3d.entity.Entity;
import de.arnomann.martin.blobby3d.entity.PointLight;
import de.arnomann.martin.blobby3d.level.Block;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL33.*;

public class Renderer {

    private static Shader defaultShader;

    private static boolean initialized;

    private static int vao;
    private static Camera camera;
    private static Shader shader;

    private static List<PointLight> pointLights = new ArrayList<>();

    private Renderer() {}

    public static void initialize() {
        if(initialized)
            return;

        if(Blobby3D.getRenderAPI() == RenderAPI.OPENGL) {
            vao = glGenVertexArrays();
            glBindVertexArray(vao);

            glEnableVertexAttribArray(0);
            glEnableVertexAttribArray(1);
            glEnableVertexAttribArray(2);

            float aspectRatio = (float) Blobby3D.getWindow().getWidth() / Blobby3D.getWindow().getHeight();
            camera = new PerspectiveCamera(70, aspectRatio, 0.01f, 1000f);

            defaultShader = Shader.createFromName("litDefault");
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

        shader.setUniformVector3f("u_AmbientLightColor", Blobby3D.getLevel().getAmbientLightColor());

        for(int i = 0; i < pointLights.size(); i++)
            shader.setUniformPointLight("u_PointLights[" + i + "]", pointLights.get(i));
        shader.setUniform1i("u_PointLightCount", pointLights.size());

        shader.setUniformVector3f("u_CameraPosition", camera.getPosition());

        shader.setUniformMatrix3f("u_NormalMatrix", new Matrix3f(block.getModelMatrix()).invert().transpose());
        shader.setUniformMatrix4f("u_ModelMatrix", block.getModelMatrix());
        shader.setUniformMatrix4f("u_ModelViewProjectionMatrix", new Matrix4f(
                camera.getViewProjectionMatrix()).mul(block.getModelMatrix()));

        glBindBuffer(GL_ARRAY_BUFFER, block.getModel().getVBO());
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 12, 0);
        glBindBuffer(GL_ARRAY_BUFFER, block.getModel().getTBO());
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 8, 0);
        glBindBuffer(GL_ARRAY_BUFFER, block.getModel().getNBO());
        glVertexAttribPointer(2, 3, GL_FLOAT, false, 12, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, block.getModel().getEBO());
        glDrawElements(GL_TRIANGLES, block.getModel().getCount(), GL_UNSIGNED_INT, 0);
    }

    public static void renderEntity(Entity entity) {
        if(entity.getModel() == null)
            return;

        shader.bind();
        entity.getTexture().bind(0);
        shader.setUniform1i("u_Texture", 0);

        shader.setUniformVector3f("u_AmbientLightColor", Blobby3D.getLevel().getAmbientLightColor());

        for(int i = 0; i < pointLights.size(); i++) {
            shader.setUniformPointLight("u_PointLights[" + i + "]", pointLights.get(i));
        }
        shader.setUniform1i("u_PointLightCount", pointLights.size());

        shader.setUniformVector3f("u_CameraPosition", camera.getPosition());

        shader.setUniformMatrix3f("u_NormalMatrix", new Matrix3f(entity.getModelMatrix()).invert().transpose());
        shader.setUniformMatrix4f("u_ModelMatrix", entity.getModelMatrix());
        shader.setUniformMatrix4f("u_ModelViewProjectionMatrix", new Matrix4f(
                camera.getViewProjectionMatrix()).mul(entity.getModelMatrix()));

        glBindBuffer(GL_ARRAY_BUFFER, entity.getModel().getVBO());
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 12, 0);
        glBindBuffer(GL_ARRAY_BUFFER, entity.getModel().getTBO());
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 8, 0);
        glBindBuffer(GL_ARRAY_BUFFER, entity.getModel().getNBO());
        glVertexAttribPointer(2, 3, GL_FLOAT, false, 12, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, entity.getModel().getEBO());
        glDrawElements(GL_TRIANGLES, entity.getModel().getCount(), GL_UNSIGNED_INT, 0);
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
