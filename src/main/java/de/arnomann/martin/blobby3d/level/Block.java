package de.arnomann.martin.blobby3d.level;

import de.arnomann.martin.blobby3d.render.texture.ITexture;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL46.*;

public class Block {

    private static int vbo, tbo, nbo;
    private int ebo;

    public Vector3f position;
    public Quaternionf rotation;
    public Vector3f dimensions;
    public ITexture texture;
    private long id;

    private static boolean initialized;

    private static long blockCount = 0;

    public Block(Vector3f position, Quaternionf rotation, Vector3f dimensions, ITexture texture,
                 boolean[] faces) {
        this.position = position;
        this.rotation = rotation;
        this.dimensions = dimensions;
        this.texture = texture;

        List<Integer> indices = new ArrayList<>();
        if(faces[0])    // TOP
            indices.addAll(List.of(23, 18, 22, 19, 18, 23));
        if(faces[1])    // DOWN
            indices.addAll(List.of(16, 21, 17, 20, 21, 16));
        if(faces[2])    // LEFT
            indices.addAll(List.of(12, 8, 11, 12, 11, 15));
        if(faces[3])    // RIGHT
            indices.addAll(List.of(13, 14, 9, 9, 14, 10));
        if(faces[4])    // FRONT
            indices.addAll(List.of(0, 1, 2, 0, 2, 3));
        if(faces[5])    // BACK
            indices.addAll(List.of(5, 4, 7, 5, 7, 6));

        ebo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, ebo);
        glBufferData(GL_ARRAY_BUFFER, indices.stream().mapToInt(i -> i).toArray(), GL_STATIC_DRAW);

        id = ++blockCount;
    }

    public static void initialize() {
        if(!initialized) {
            initialized = true;

            float[] vertices = new float[] {
                    -0.5f, -0.5f, -0.5f, // 0   FRONT & BACK
                     0.5f, -0.5f, -0.5f, // 1
                     0.5f,  0.5f, -0.5f, // 2
                    -0.5f,  0.5f, -0.5f, // 3
                    -0.5f, -0.5f,  0.5f, // 4
                     0.5f, -0.5f,  0.5f, // 5
                     0.5f,  0.5f,  0.5f, // 6
                    -0.5f,  0.5f,  0.5f, // 7
                    -0.5f, -0.5f, -0.5f, // 8   LEFT & RIGHT
                     0.5f, -0.5f, -0.5f, // 9
                     0.5f,  0.5f, -0.5f, // 10
                    -0.5f,  0.5f, -0.5f, // 11
                    -0.5f, -0.5f,  0.5f, // 12
                     0.5f, -0.5f,  0.5f, // 13
                     0.5f,  0.5f,  0.5f, // 14
                    -0.5f,  0.5f,  0.5f, // 15
                    -0.5f, -0.5f, -0.5f, // 16  TOP & BOTTOM
                     0.5f, -0.5f, -0.5f, // 17
                     0.5f,  0.5f, -0.5f, // 18
                    -0.5f,  0.5f, -0.5f, // 19
                    -0.5f, -0.5f,  0.5f, // 20
                     0.5f, -0.5f,  0.5f, // 21
                     0.5f,  0.5f,  0.5f, // 22
                    -0.5f,  0.5f,  0.5f  // 23
            };
            float[] textureCoords = new float[] {
                    0f, 0f, // 0
                    0f, 1f, // 1
                    1f, 1f, // 2
                    1f, 0f, // 3
                    0f, 1f, // 4
                    1f, 1f, // 5
                    1f, 0f, // 6
                    0f, 0f, // 7
                    0f, 0f, // 8
                    0f, 1f, // 9
                    1f, 1f, // 10
                    1f, 0f, // 11
                    0f, 1f, // 12
                    1f, 1f, // 13
                    1f, 0f, // 14
                    0f, 0f, // 15
                    0f, 0f, // 16
                    0f, 1f, // 17
                    1f, 1f, // 18
                    1f, 0f, // 19
                    0f, 1f, // 20
                    1f, 1f, // 21
                    1f, 0f, // 22
                    0f, 0f  // 23
            };
            float[] normals = new float[] {
                     0f,  0f, -1f, // 0   FRONT & BACK
                     0f,  0f, -1f, // 1
                     0f,  0f, -1f, // 2
                     0f,  0f, -1f, // 3
                     0f,  0f,  1f, // 4
                     0f,  0f,  1f, // 5
                     0f,  0f,  1f, // 6
                     0f,  0f,  1f, // 7
                    -1f,  0f,  0f, // 8   LEFT & RIGHT
                     1f,  0f,  0f, // 9
                     1f,  0f,  0f, // 10
                    -1f,  0f,  0f, // 11
                    -1f,  0f,  0f, // 12
                     1f,  0f,  0f, // 13
                     1f,  0f,  0f, // 14
                    -1f,  0f,  0f, // 15
                     0f, -1f,  0f, // 16  TOP & BOTTOM
                     0f, -1f,  0f, // 17
                     0f,  1f,  0f, // 18
                     0f,  1f,  0f, // 19
                     0f, -1f,  0f, // 20
                     0f, -1f,  0f, // 21
                     0f,  1f,  0f, // 22
                     0f,  1f,  0f  // 23
            };
            int[] indices = new int[] {
                    0,  1,  2,
                    0,  2,  3,
                    12, 8,  11,
                    12, 11, 15,
                    5,  4,  7,
                    5,  7,  6,
                    13, 14, 9,
                    9,  14, 10,
                    23, 18, 22,
                    19, 18, 23,
                    16, 21, 17,
                    20, 21, 16
            };

            vbo = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, vbo);
            glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);

            tbo = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, tbo);
            glBufferData(GL_ARRAY_BUFFER, textureCoords, GL_STATIC_DRAW);

            nbo = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, nbo);
            glBufferData(GL_ARRAY_BUFFER, normals, GL_STATIC_DRAW);
        }
    }

    public long getId() {
        return id;
    }

    public int getFaceCount() {
        return 6;
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

    public Matrix4f getModelMatrix() {
        return new Matrix4f().translate(position).scale(dimensions).rotate(rotation);
    }

}
