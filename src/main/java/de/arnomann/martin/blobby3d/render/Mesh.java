package de.arnomann.martin.blobby3d.render;

import de.arnomann.martin.blobby3d.core.Blobby3D;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL33.*;

public class Mesh {

    private int count;
    private int vbo, tbo, nbo, ebo;
    private final boolean isDynamic;

    private float[] vertices, textureCoords, normals;
    private int[] indices;

    public Mesh(float[] vertices, float[] textureCoords, float[] normals, int[] indices) {
        this(vertices, textureCoords, normals, indices, false);
    }

    public Mesh(float[] vertices, float[] textureCoords, float[] normals, int[] indices,
                boolean isDynamic) {
        this.count = indices.length;
        this.isDynamic = isDynamic;

        this.vertices = vertices;
        this.textureCoords = textureCoords;
        this.normals = normals;
        this.indices = indices;

        if(Blobby3D.getRenderAPI() == RenderAPI.OPENGL) {
            FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(vertices.length);
            verticesBuffer.put(vertices);
            verticesBuffer.flip();
            vbo = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, vbo);
            glBufferData(GL_ARRAY_BUFFER, verticesBuffer, isDynamic ? GL_DYNAMIC_DRAW : GL_STATIC_DRAW);

            FloatBuffer textureCoordsBuffer = BufferUtils.createFloatBuffer(textureCoords.length);
            textureCoordsBuffer.put(textureCoords);
            textureCoordsBuffer.flip();
            tbo = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, tbo);
            glBufferData(GL_ARRAY_BUFFER, textureCoordsBuffer, isDynamic ? GL_DYNAMIC_DRAW : GL_STATIC_DRAW);

            FloatBuffer normalsBuffer = BufferUtils.createFloatBuffer(normals.length);
            normalsBuffer.put(normals);
            normalsBuffer.flip();
            nbo = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, nbo);
            glBufferData(GL_ARRAY_BUFFER, normalsBuffer, isDynamic ? GL_DYNAMIC_DRAW : GL_STATIC_DRAW);

            IntBuffer indicesBuffer = BufferUtils.createIntBuffer(indices.length);
            indicesBuffer.put(indices);
            indicesBuffer.flip();
            ebo = glGenBuffers();
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, isDynamic ? GL_DYNAMIC_DRAW : GL_STATIC_DRAW);

            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        }
    }

    public void setVertices(float[] vertices) {
        if(!isDynamic)
            return;

        if(Blobby3D.getRenderAPI() == RenderAPI.OPENGL) {
            FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(vertices.length);
            verticesBuffer.put(vertices);
            verticesBuffer.flip();
            glBindBuffer(GL_ARRAY_BUFFER, vbo);
            glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_DYNAMIC_DRAW);
        }
    }

    public void setTextureCoords(float[] textureCoords) {
        if(!isDynamic)
            return;

        if(Blobby3D.getRenderAPI() == RenderAPI.OPENGL) {
            FloatBuffer textureCoordsBuffer = BufferUtils.createFloatBuffer(textureCoords.length);
            textureCoordsBuffer.put(textureCoords);
            textureCoordsBuffer.flip();
            glBindBuffer(GL_ARRAY_BUFFER, tbo);
            glBufferData(GL_ARRAY_BUFFER, textureCoordsBuffer, GL_DYNAMIC_DRAW);
        }
    }

    public void setNormals(float[] normals) {
        if(!isDynamic)
            return;

        if(Blobby3D.getRenderAPI() == RenderAPI.OPENGL) {
            FloatBuffer normalsBuffer = BufferUtils.createFloatBuffer(normals.length);
            normalsBuffer.put(normals);
            normalsBuffer.flip();
            glBindBuffer(GL_ARRAY_BUFFER, nbo);
            glBufferData(GL_ARRAY_BUFFER, normalsBuffer, GL_DYNAMIC_DRAW);
        }
    }

    public void setIndices(int[] indices) {
        if(!isDynamic)
            return;

        count = indices.length;

        if(Blobby3D.getRenderAPI() == RenderAPI.OPENGL) {
            IntBuffer indicesBuffer = BufferUtils.createIntBuffer(indices.length);
            indicesBuffer.put(indices);
            indicesBuffer.flip();
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_DYNAMIC_DRAW);
        }
    }

    public int getCount() {
        return count;
    }

    public int getVBO() {
        return vbo;
    }

    public int getTBO() {
        return tbo;
    }

    public int getNBO() {
        return nbo;
    }

    public int getEBO() {
        return ebo;
    }

}
