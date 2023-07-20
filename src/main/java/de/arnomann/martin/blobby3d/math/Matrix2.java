package de.arnomann.martin.blobby3d.math;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.util.Arrays;

public class Matrix2 {

    private float[][] content;

    public Matrix2() {
        content = new float[2][2];
        content[0][0] = 1f;
        content[1][1] = 1f;
    }

    public Matrix2(float m00, float m01,
                   float m10, float m11) {
        content = new float[][] {
                { m00, m01 },
                { m10, m11 }
        };
    }

    public Matrix2(Matrix2 matrix) {
        this.content = Arrays.copyOf(matrix.content, 4);
    }

    public static Matrix2 identity() {
        return new Matrix2();
    }

    public Matrix2 set(int i, int j, float val) {
        content[i][j] = val;
        return this;
    }

    public float get(int i, int j) {
        return content[i][j];
    }

    public Matrix2 add(Matrix2 m) {
        Matrix2 out = new Matrix2();
        for(int i = 0; i < 2; i++) {
            for(int j = 0; j < 2; j++) {
                out.content[i][j] = this.content[i][j] + m.content[i][j];
            }
        }
        return out;
    }

    public Matrix2 mul(Matrix2 m) {
        Matrix2 out = new Matrix2();
        for(int i = 0; i < 2; i++) {
            for(int j = 0; j < 2; j++) {
                out.content[i][j] = this.content[i][0] * m.content[0][j] +
                                    this.content[i][1] * m.content[1][j];
            }
        }
        return out;
    }

    public Matrix2 mul(float scalar) {
        Matrix2 out = new Matrix2();
        for(int i = 0; i < 2; i++) {
            for(int j = 0; j < 2; j++) {
                out.content[i][j] = this.content[i][j] * scalar;
            }
        }
        return out;
    }

    public Vector2 mul(Vector2 v) {
        Vector2 out = new Vector2();
        out.x = content[0][0] * v.x + content[0][1] * v.y;
        out.y = content[1][0] * v.x + content[1][1] * v.y;
        return out;
    }

    public float determinant() {
        return content[0][0] * content[1][1] -
               content[1][0] * content[0][1];
    }

    public Matrix2 inverse() {
        float determinant = determinant();
        if(determinant == 0)
            return null;
        return new Matrix2( content[1][1], -content[0][1],
                           -content[1][0],  content[0][0])
                .mul(1 / determinant);
    }

    public Matrix2 transpose() {
        Matrix2 out = new Matrix2();
        for(int i = 0; i < 2; i++) {
            for(int j = 0; j < 2; j++) {
                out.set(j, i, content[i][j]);
            }
        }
        return out;
    }

    public FloatBuffer toFloatBuffer() {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(4);
        for(int i = 0; i < 4; i++) {
            buffer.put(i, content[i / 2][i % 2]);
        }
        return buffer;
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder("( ");

        for(int i = 0; i < 2; i++) {
            for(int j = 0; j < 2; j++) {
                string.append(content[i][j]).append(", ");
            }
            string.append("\n  ");
        }
        string.delete(string.length() - 5, string.length()).append(" )");

        return string.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;
        if(obj == null || getClass() != obj.getClass())
            return false;
        return Arrays.deepEquals(content, ((Matrix2) obj).content);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(content);
    }

}
