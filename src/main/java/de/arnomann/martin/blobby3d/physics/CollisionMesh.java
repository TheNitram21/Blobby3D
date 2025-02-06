package de.arnomann.martin.blobby3d.physics;

import de.arnomann.martin.blobby3d.math.Vector3;

import java.util.List;

public class CollisionMesh {

    private List<Vector3> vertices, normals, edges;
    private float radius;

    public CollisionMesh(List<Vector3> vertices, List<Vector3> normals, List<Vector3> edges) {
        this.vertices = vertices;
        this.normals = normals;
        this.edges = edges;
        for(int i = 0; i < vertices.size(); i++) {
            float distance = vertices.get(i).length();
            if(distance > radius)
                radius = distance;
        }
    }

    public List<Vector3> getVertices() {
        return vertices;
    }

    public List<Vector3> getNormals() {
        return normals;
    }

    public List<Vector3> getEdges() {
        return edges;
    }

    public float getRadius() {
        return radius;
    }

}
