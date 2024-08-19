package de.arnomann.martin.blobby3d.math;

import java.util.Objects;

public class Vector2 {

    public static final Vector2 zero = new Vector2();
    public static final Vector2 up = new Vector2(0, 1);
    public static final Vector2 down = new Vector2(0, -1);
    public static final Vector2 left = new Vector2(-1, 0);
    public static final Vector2 right = new Vector2(1, 0);

    public float x, y;

    public Vector2() {
        this(0f, 0f);
    }

    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2(Vector2 v) {
        this.x = v.x;
        this.y = v.y;
    }

    public Vector2 add(Vector2 v) {
        return new Vector2(this.x + v.x, this.y + v.y);
    }

    public Vector2 add(float scalar) {
        return new Vector2(this.x + scalar, this.y + scalar);
    }

    public Vector2 sub(Vector2 v) {
        return new Vector2(this.x - v.x, this.y - v.y);
    }

    public Vector2 sub(float scalar) {
        return new Vector2(this.x - scalar, this.y - scalar);
    }

    public Vector2 mul(float scalar) {
        return new Vector2(this.x * scalar, this.y * scalar);
    }

    public Vector2 div(float scalar) {
        return new Vector2(this.x / scalar, this.y / scalar);
    }

    public float length() {
        return (float) Math.sqrt(x * x + y * y);
    }

    public float lengthSquared() {
        return x * x + y * y;
    }

    public Vector2 normalized() {
        float length = length();
        if(length == 0)
            return this;
        return new Vector2(x / length, y / length);
    }

    public float dot(Vector2 v) {
        return this.x * v.x + this.y * v.y;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;
        if(obj == null || getClass() != obj.getClass())
            return false;
        Vector2 v = (Vector2) obj;
        return this.x == v.x && this.y == v.y;
    }

    @Override
    public String toString() {
        return "[" + x + " | " + y + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

}
