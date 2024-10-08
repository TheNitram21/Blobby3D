package de.arnomann.martin.blobby3d.level;

import de.arnomann.martin.blobby3d.physics.Collider;
import de.arnomann.martin.blobby3d.physics.CollisionMesh;
import de.arnomann.martin.blobby3d.render.texture.ITexture;
import de.arnomann.martin.blobby3d.math.*;
import de.arnomann.martin.blobby3d.render.texture.TextureData;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL46.*;

public class Block implements Collider {

    private static int vbo, nbo;
    private int tbo, ebo;
    private static CollisionMesh collisionMesh;

    public Vector3 position;
    public Quaternion rotation;
    public Vector3 dimensions;
    public ITexture texture;
    private long id;

    private static boolean initialized;

    private static long blockCount = 0;

    public Block(Vector3 position, Quaternion rotation, Vector3 dimensions, ITexture texture,
                 boolean[] faces) {
        this.position = position;
        this.rotation = rotation;
        this.dimensions = dimensions;
        this.texture = texture;

        float[] textureCoords;
        if(texture.getData().stretch == TextureData.StretchMode.FIT) {
            textureCoords = new float[]{
                    1f, 1f, // 0
                    0f, 1f, // 1
                    0f, 0f, // 2
                    1f, 0f, // 3
                    0f, 1f, // 4
                    1f, 1f, // 5
                    1f, 0f, // 6
                    0f, 0f, // 7
                    0f, 1f, // 8
                    1f, 1f, // 9
                    1f, 0f, // 10
                    0f, 0f, // 11
                    1f, 1f, // 12
                    0f, 1f, // 13
                    0f, 0f, // 14
                    1f, 0f, // 15
                    0f, 0f, // 16
                    0f, 1f, // 17
                    1f, 1f, // 18
                    1f, 0f, // 19
                    1f, 0f, // 20
                    1f, 1f, // 21
                    0f, 1f, // 22
                    0f, 0f  // 23
            };
        } else {
            textureCoords = new float[]{
                    dimensions.x, dimensions.y, // 0
                    0f, dimensions.y, // 1
                    0f, 0f, // 2
                    dimensions.x, 0f, // 3
                    0f, dimensions.y, // 4
                    dimensions.x, dimensions.y, // 5
                    dimensions.x, 0f, // 6
                    0f, 0f, // 7
                    0f, dimensions.y, // 8
                    dimensions.z, dimensions.y, // 9
                    dimensions.z, 0f, // 10
                    0f, 0f, // 11
                    dimensions.z, dimensions.y, // 12
                    0f, dimensions.y, // 13
                    0f, 0f, // 14
                    dimensions.z, 0f, // 15
                    0f, 0f, // 16
                    0f, dimensions.z, // 17
                    dimensions.x, dimensions.z, // 18
                    dimensions.x, 0f, // 19
                    dimensions.x, 0f, // 20
                    dimensions.x, dimensions.z, // 21
                    0f, dimensions.z, // 22
                    0f, 0f  // 23
            };
        }

        tbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, tbo);
        glBufferData(GL_ARRAY_BUFFER, textureCoords, GL_STATIC_DRAW);

        List<Integer> indices = new ArrayList<>();
        if(faces[0])    // TOP
            indices.addAll(List.of(22, 18, 23, 23, 18, 19));
        if(faces[1])    // DOWN
            indices.addAll(List.of(17, 21, 16, 16, 21, 20));
        if(faces[2])    // LEFT
            indices.addAll(List.of(11, 8, 12, 15, 11, 12));
        if(faces[3])    // RIGHT
            indices.addAll(List.of(9, 14, 13, 10, 14, 9));
        if(faces[4])    // FRONT
            indices.addAll(List.of(2, 1, 0, 3, 2, 0));
        if(faces[5])    // BACK
            indices.addAll(List.of(7, 4, 5, 6, 7, 5));

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

            vbo = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, vbo);
            glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);

            nbo = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, nbo);
            glBufferData(GL_ARRAY_BUFFER, normals, GL_STATIC_DRAW);

            collisionMesh = new CollisionMesh(vertices, normals);
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

    public Vector3 getPosition() {
        return position;
    }

    public Vector3 getScale() {
        return dimensions;
    }

    public Matrix4 getModelMatrix() {
        return new Matrix4().translate(position).scale(dimensions).rotate(rotation);
    }

    public CollisionMesh getCollisionMesh() {
        return collisionMesh;
    }

}
