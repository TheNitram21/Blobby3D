package de.arnomann.martin.blobby3d.render;

import de.arnomann.martin.blobby3d.math.*;

public class OrthographicCamera implements Camera {

    private Matrix4 projectionMatrix;
    private Matrix4 viewMatrix;
    private Matrix4 viewProjectionMatrix;

    private Vector3 position;
    private Quaternion rotation;

    public OrthographicCamera(float left, float right, float bottom, float top, float near, float far) {
        position = new Vector3();
        rotation = new Quaternion();

        projectionMatrix = Matrix4.ortho(left, right, bottom, top, near, far);
        viewMatrix = new Matrix4();
        viewProjectionMatrix = new Matrix4(projectionMatrix).mul(viewMatrix);
    }

    @Override
    public Matrix4 getProjectionMatrix() {
        return projectionMatrix;
    }

    @Override
    public Matrix4 getViewMatrix() {
        return viewMatrix;
    }

    @Override
    public Matrix4 getViewProjectionMatrix() {
        return viewProjectionMatrix;
    }

    @Override
    public Vector3 getPosition() {
        return position;
    }

    @Override
    public void setPosition(Vector3 position) {
        this.position = position;
        recalculateViewMatrix();
    }

    @Override
    public Quaternion getRotation() {
        return new Quaternion(rotation);
    }

    @Override
    public void setRotation(Quaternion rotation) {
        this.rotation = rotation;
        recalculateViewMatrix();
    }

    @Override
    public Vector3 getForward() {
        return rotation.rotate(Vector3.forward);
    }

    @Override
    public Vector3 getRight() {
        return rotation.rotate(Vector3.right);
    }

    @Override
    public Vector3 getUp() {
        return getForward().cross(getRight());
    }

    public void recalculateViewMatrix() {
        viewMatrix = new Matrix4().translate(position).rotate(rotation).inverse();
        viewProjectionMatrix = new Matrix4(projectionMatrix).mul(viewMatrix);
    }

}
