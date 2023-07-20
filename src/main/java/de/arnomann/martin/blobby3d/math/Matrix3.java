package de.arnomann.martin.blobby3d.math;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.util.Arrays;

public class Matrix3 {

    private float[][] content;

    public Matrix3() {
        content = new float[3][3];
        content[0][0] = 1f;
        content[1][1] = 1f;
        content[2][2] = 1f;
    }

    public Matrix3(float m00, float m01, float m02,
                   float m10, float m11, float m12,
                   float m20, float m21, float m22) {
        content = new float[][] {
                { m00, m01, m02 },
                { m10, m11, m12 },
                { m20, m21, m22 }
        };
    }

    public Matrix3(Matrix3 matrix) {
        this.content = Arrays.copyOf(matrix.content, 9);
    }

    public static Matrix3 identity() {
        return new Matrix3();
    }

    public Matrix3 set(int i, int j, float val) {
        content[i][j] = val;
        return this;
    }

    public float get(int i, int j) {
        return content[i][j];
    }

    public Matrix3 add(Matrix3 m) {
        Matrix3 out = new Matrix3();
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                out.content[i][j] = this.content[i][j] + m.content[i][j];
            }
        }
        return out;
    }

    public Matrix3 mul(Matrix3 m) {
        Matrix3 out = new Matrix3();
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                out.content[i][j] = this.content[i][0] * m.content[0][j] +
                                    this.content[i][1] * m.content[1][j] +
                                    this.content[i][2] * m.content[2][j];
            }
        }
        return out;
    }

    public Matrix3 mul(float scalar) {
        Matrix3 out = new Matrix3();
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                out.content[i][j] = this.content[i][j] * scalar;
            }
        }
        return out;
    }

    public Vector3 mul(Vector3 v) {
        Vector3 out = new Vector3();
        out.x = content[0][0] * v.x + content[0][1] * v.y + content[0][2] * v.z;
        out.y = content[1][0] * v.x + content[1][1] * v.y + content[1][2] * v.z;
        out.z = content[2][0] * v.x + content[2][1] * v.y + content[2][2] * v.z;
        return out;
    }

    public float determinant() {
        return content[0][0] * content[1][1] * content[2][2] +
               content[0][1] * content[1][2] * content[2][0] +
               content[0][2] * content[1][0] * content[2][1] -
               content[0][0] * content[1][2] * content[2][1] -
               content[0][1] * content[1][0] * content[2][2] -
               content[0][2] * content[1][1] * content[2][0];
    }

    public Matrix2 cofactor(int i, int j) {
        Matrix2 m = new Matrix2();
        for(int row = 0; row < 3; row++) {
            for(int col = 0; col < 3; col++) {
                if(row == i || col == j)
                    continue;
                m.set(row > i ? row - 1 : row, col > j ? col - 1 : col, content[row][col]);
            }
        }
        return m;
    }

    public Matrix3 adjoint() {
        Matrix3 m = new Matrix3();
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                Matrix2 c = cofactor(i, j);
                int sign = (i + j) % 2 == 0 ? 1 : -1;
                m.set(i, j, sign * c.determinant());
            }
        }
        return m.transpose();
    }

    public Matrix3 inverse() {
        float determinant = determinant();
        if(determinant == 0)
            return null;

        return adjoint().mul(1 / determinant);
    }

    public Matrix3 transpose() {
        Matrix3 out = new Matrix3();
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                out.set(j, i, content[i][j]);
            }
        }
        return out;
    }

    public FloatBuffer toFloatBuffer() {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(9);
        for(int i = 0; i < 9; i++) {
            buffer.put(i, content[i / 3][i % 3]);
        }
        return buffer;
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder("( ");

        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
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
        return Arrays.deepEquals(content, ((Matrix3) obj).content);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(content);
    }

}
