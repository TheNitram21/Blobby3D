package de.arnomann.martin.blobby3d.render;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public interface Camera {

    Matrix4f getProjectionMatrix();
    Matrix4f getViewMatrix();
    Matrix4f getViewProjectionMatrix();

    Vector3f getPosition();
    void setPosition(Vector3f position);
    Quaternionf getRotation();
    void setRotation(Quaternionf rotation);

    Vector3f getForward();
    Vector3f getRight();
    Vector3f getUp();

    void recalculateViewMatrix();

}
