package de.arnomann.martin.blobby3d.physics;

import de.arnomann.martin.blobby3d.math.Matrix4;
import de.arnomann.martin.blobby3d.math.Vector3;

public interface Collider {
    Vector3 getPosition();
    Vector3 getScale();
    Matrix4 getModelMatrix();
    CollisionMesh getCollisionMesh();
}
