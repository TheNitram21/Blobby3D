package de.arnomann.martin.blobby3d.math;

import java.util.Objects;

public class Vector3 {

    public static final Vector3 zero = new Vector3();
    public static final Vector3 up = new Vector3(0, 1, 0);
    public static final Vector3 down = new Vector3(0, -1, 0);
    public static final Vector3 left = new Vector3(-1, 0, 0);
    public static final Vector3 right = new Vector3(1, 0,0 );
    public static final Vector3 forward = new Vector3(0, 0, 1);
    public static final Vector3 back = new Vector3(0, 0, -1);

    public float x, y, z;

    public Vector3() {
        this(0f, 0f, 0f);
    }

    public Vector3(Vector2 xy) {
        this(xy, 0f);
    }

    public Vector3(Vector2 xy, float z) {
        this(xy.x, xy.y, z);
    }

    public Vector3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3(Vector3 v) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
    }

    public Vector3 add(Vector3 v) {
        return new Vector3(this.x + v.x, this.y + v.y, this.z + v.z);
    }

    public Vector3 add(float scalar) {
        return new Vector3(this.x + scalar, this.y + scalar, this.z + scalar);
    }

    public Vector3 sub(Vector3 v) {
        return new Vector3(this.x - v.x, this.y - v.y, this.z - v.z);
    }

    public Vector3 sub(float scalar) {
        return new Vector3(this.x - scalar, this.y - scalar, this.z - scalar);
    }

    public Vector3 mul(float scalar) {
        return new Vector3(this.x * scalar, this.y * scalar, this.z * scalar);
    }

    public Vector3 div(float scalar) {
        return new Vector3(this.x / scalar, this.y / scalar, this.z / scalar);
    }

    public float length() {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }

    public Vector3 normalized() {
        float length = length();
        if(length == 0)
            return this;
        return new Vector3(x / length, y / length, z / length);
    }

    public float dot(Vector3 v) {
        return this.x * v.x + this.y * v.y + this.z * v.z;
    }

    public Vector3 cross(Vector3 v) {
        float x = this.y * v.z - this.z * v.y;
        float y = this.z * v.x - this.x * v.z;
        float z = this.x * v.y - this.y * v.x;
        return new Vector3(x, y, z);
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;
        if(obj == null || getClass() != obj.getClass())
            return false;
        Vector3 v = (Vector3) obj;
        return this.x == v.x && this.y == v.y && this.z == v.z;
    }

    @Override
    public String toString() {
        return "[" + x + " | " + y + " | " + z + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

}
