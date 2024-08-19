package de.arnomann.martin.blobby3d.physics;

import de.arnomann.martin.blobby3d.math.Vector3;

import java.util.ArrayList;
import java.util.List;

public class CollisionMesh {

    private List<Vector3> vertices, normals;
    private float radius;

    public CollisionMesh(float[] vertices, float[] normals) {
        if(vertices.length % 3 != 0 || normals.length % 3 != 0)
            throw new IllegalArgumentException("Illegal input array length.");

        this.vertices = new ArrayList<>(vertices.length / 3);
        this.normals = new ArrayList<>(normals.length / 3);
        for(int i = 0; i < vertices.length; i += 3) {
            Vector3 vertex = new Vector3(vertices[i], vertices[i + 1], vertices[i + 2]);
            this.vertices.add(vertex);

            float distance = vertex.length();
            if(distance > radius)
                radius = distance;
        }
        for(int i = 0; i < normals.length; i += 3) {
            this.normals.add(new Vector3(normals[i], normals[i + 1], normals[i + 2]));
        }
    }

    public List<Vector3> getVertices() {
        return vertices;
    }

    public List<Vector3> getNormals() {
        return normals;
    }

    public float getRadius() {
        return radius;
    }

}
