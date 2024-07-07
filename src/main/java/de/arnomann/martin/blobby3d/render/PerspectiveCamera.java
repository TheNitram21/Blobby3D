package de.arnomann.martin.blobby3d.render;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class PerspectiveCamera implements Camera {

    private Matrix4f projectionMatrix;
    private Matrix4f viewMatrix;
    private Matrix4f viewProjectionMatrix;

    private Vector3f position;
    private Quaternionf rotation;

    public PerspectiveCamera(float fovy, float aspectRatio, float near, float far) {
        position = new Vector3f();
        rotation = new Quaternionf();

        projectionMatrix = new Matrix4f().perspectiveLH((float) Math.toRadians(fovy), aspectRatio, near, far);
        viewMatrix = new Matrix4f();
        viewProjectionMatrix = new Matrix4f(projectionMatrix).mul(viewMatrix);
    }

    @Override
    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    @Override
    public Matrix4f getViewMatrix() {
        return viewMatrix;
    }

    @Override
    public Matrix4f getViewProjectionMatrix() {
        return viewProjectionMatrix;
    }

    @Override
    public Vector3f getPosition() {
        return new Vector3f(position);
    }

    @Override
    public void setPosition(Vector3f position) {
        this.position = position;
        recalculateViewMatrix();
    }

    @Override
    public Quaternionf getRotation() {
        return new Quaternionf(rotation);
    }

    @Override
    public void setRotation(Quaternionf rotation) {
        this.rotation = rotation;
        recalculateViewMatrix();
    }

    @Override
    public Vector3f getForward() {
        return new Vector3f(0f, 0f, 1f).rotate(rotation);
    }

    @Override
    public Vector3f getRight() {
        return new Vector3f(1f, 0f, 0f).rotate(rotation);
    }

    @Override
    public Vector3f getUp() {
        return getForward().cross(getRight());
    }

    public void recalculateViewMatrix() {
        viewMatrix = new Matrix4f().translate(position).rotate(rotation).invert();
        viewProjectionMatrix = new Matrix4f(projectionMatrix).mul(viewMatrix);
    }

}
