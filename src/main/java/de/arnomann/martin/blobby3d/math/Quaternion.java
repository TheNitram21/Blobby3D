package de.arnomann.martin.blobby3d.math;

public class Quaternion {

    private float x, y, z, w;

    public Quaternion() {
        this(0f, 0f, 0f, 1f);
    }

    public Quaternion(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Quaternion(Quaternion q) {
        this(q.x, q.y, q.z, q.w);
    }

    public static Quaternion fromEulerAngles(float x, float y, float z) {
        float xCos = (float) Math.cos(x * 0.5f);
        float xSin = (float) Math.sin(x * 0.5f);
        float yCos = (float) Math.cos(y * 0.5f);
        float ySin = (float) Math.sin(y * 0.5f);
        float zCos = (float) Math.cos(z * 0.5f);
        float zSin = (float) Math.sin(z * 0.5f);

        return new Quaternion(xSin * yCos * zCos - xCos * ySin * zSin,
                              xCos * ySin * zCos + xSin * yCos * zSin,
                              xCos * yCos * zSin - xSin * ySin * zCos,
                              xCos * yCos * zCos + xSin * ySin * zSin);
    }
    public Vector3 asEulerAngles() {
        return null; // TODO
    }

    public Quaternion add(Quaternion q) {
        return new Quaternion(this.x + q.x, this.y + q.y, this.z + q.z, this.w + q.w);
    }

    public Quaternion sub(Quaternion q) {
        return new Quaternion(this.x - q.x, this.y - q.y, this.z - q.z, this.w - q.w);
    }

    public Quaternion mul(Quaternion q) {
        float x = q.x * this.w + this.x * q.w + this.y * q.z - this.z * q.y;
        float y = q.y * this.w + this.y * q.w + this.z * q.x - this.x * q.z;
        float z = q.z * this.w + this.z * q.w + this.x * q.y - this.y * q.x;
        float w = this.w * q.w - this.x * q.x - this.y * q.y - this.z * q.z;
        return new Quaternion(x, y, z, w);
    }

    public Quaternion mul(float scalar) {
        return new Quaternion(x * scalar, y * scalar, z * scalar, w * scalar);
    }

    public Vector3 rotate(Vector3 v) {
        Quaternion vectorAsQuat = new Quaternion(v.x, v.y, v.z, 0);
        Quaternion hamilton = hamilton(vectorAsQuat).hamilton(conjugate());
        return new Vector3(hamilton.x, hamilton.y, hamilton.z);
    }

    public float length() {
        return (float) Math.sqrt(x * x + y * y + z * z + w * w);
    }

    public Quaternion normalized() {
        float length = length();
        if(length == 0)
            return this;
        return new Quaternion(x / length, y / length, z / length, w / length);
    }

    public Quaternion conjugate() {
        return new Quaternion(-x, -y, -z, w);
    }

    public Quaternion inverse() {
        float sqrLength = length();
        sqrLength *= sqrLength;

        Quaternion c = conjugate();
        return new Quaternion(c.x / sqrLength, c.y / sqrLength, c.z / sqrLength, c.w / sqrLength);
    }

    public Quaternion hamilton(Quaternion q) {
        return new Quaternion(this.w * q.x + this.x * q.w + this.y * q.z - this.z * q.y,
                              this.w * q.y - this.x * q.z + this.y * q.w + this.z * q.x,
                              this.w * q.z + this.x * q.y - this.y * q.x + this.z * q.w,
                              this.w * q.w - this.x * q.x - this.y * q.y - this.z * q.z);
    }

    public Matrix4 toRotationMatrix() {
        return new Matrix4(w * w + x * x - y * y - z * z, 2 * x * y - 2 * w * z, 2 * x * z + 2 * w * y,
                                    0f,
                            2 * x * y + 2 * w * z, w * w - x * x + y * y - z * z, 2 * y * z + 2 * w * x,
                                    0f,
                            2 * x * z - 2 * w * y, 2 * y * z - 2 * w * x, w * w - x * x - y * y + z * z,
                                    0f,
                            0f, 0f, 0f, 1f);
    }

}
