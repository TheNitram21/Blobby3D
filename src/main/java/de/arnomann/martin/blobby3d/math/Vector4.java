package de.arnomann.martin.blobby3d.math;

import java.util.Objects;

public class Vector4 {

    public float x, y, z, w;

    public Vector4() {
        this(0f, 0f, 0f, 0f);
    }

    public Vector4(Vector2 xy, float z, float w) {
        this(xy.x, xy.y, z, w);
    }

    public Vector4(Vector3 xyz, float w) {
        this(xyz.x, xyz.y, xyz.z, w);
    }

    public Vector4(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Vector4(Vector4 v) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
        this.w = v.w;
    }

    public Vector4 add(Vector4 v) {
        return new Vector4(this.x + v.x, this.y + v.y, this.z + v.z, this.w + v.w);
    }

    public Vector4 add(float scalar) {
        return new Vector4(this.x + scalar, this.y + scalar, this.z + scalar, this.w + scalar);
    }

    public Vector4 sub(Vector4 v) {
        return new Vector4(this.x - v.x, this.y - v.y, this.z - v.z, this.w - v.w);
    }

    public Vector4 sub(float scalar) {
        return new Vector4(this.x - scalar, this.y - scalar, this.z - scalar, this.w - scalar);
    }

    public Vector4 mul(float scalar) {
        return new Vector4(this.x * scalar, this.y * scalar, this.z * scalar, this.w * scalar);
    }

    public Vector4 div(float scalar) {
        return new Vector4(this.x / scalar, this.y / scalar, this.z / scalar, this.w / scalar);
    }

    public float length() {
        return (float) Math.sqrt(x * x + y * y + z * z + w * w);
    }

    public float lengthSquared() {
        return x * x + y * y + z * z + w * w;
    }

    public Vector4 normalized() {
        float length = length();
        if(length == 0)
            return this;
        return new Vector4(x / length, y / length, z / length, w / length);
    }

    public float dot(Vector4 v) {
        return this.x * v.x + this.y * v.y + this.z * v.z + this.w * v.w;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;
        if(obj == null || getClass() != obj.getClass())
            return false;
        Vector4 v = (Vector4) obj;
        return this.x == v.x && this.y == v.y && this.z == v.z && this.w == v.w;
    }

    @Override
    public String toString() {
        return "[" + x + " | " + y + " | " + z + " | " + w + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z, w);
    }

}
