package de.arnomann.martin.blobby3d.render;

import de.arnomann.martin.blobby3d.math.Matrix4;
import de.arnomann.martin.blobby3d.math.Quaternion;
import de.arnomann.martin.blobby3d.math.Vector3;

public interface Camera {

    Matrix4 getProjectionMatrix();
    Matrix4 getViewMatrix();
    Matrix4 getViewProjectionMatrix();

    Vector3 getPosition();
    void setPosition(Vector3 position);
    Quaternion getRotation();
    void setRotation(Quaternion rotation);

    Vector3 getForward();
    Vector3 getRight();
    Vector3 getUp();

    void recalculateViewMatrix();

}
