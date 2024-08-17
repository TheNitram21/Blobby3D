package de.arnomann.martin.blobby3d.math;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.util.Arrays;

public class Matrix4 {

    private float[][] content;

    public Matrix4() {
        content = new float[4][4];
        content[0][0] = 1f;
        content[1][1] = 1f;
        content[2][2] = 1f;
        content[3][3] = 1f;
    }

    public Matrix4(float m00, float m01, float m02, float m03,
                   float m10, float m11, float m12, float m13,
                   float m20, float m21, float m22, float m23,
                   float m30, float m31, float m32, float m33) {
        content = new float[][] {
                { m00, m01, m02, m03 },
                { m10, m11, m12, m13 },
                { m20, m21, m22, m23 },
                { m30, m31, m32, m33 }
        };
    }

    public Matrix4(Matrix4 matrix) {
        this.content = Arrays.copyOf(matrix.content, 16);
    }

    public static Matrix4 identity() {
        return new Matrix4();
    }

    public static Matrix4 ortho(float left, float right, float bottom, float top, float near, float far) {
        return new Matrix4(2f / (right - left), 0f, 0f, -(right + left) / (right - left),
                0f, 2f / (top - bottom), 0f, -(top + bottom) / (top - bottom),
                0f, 0f, -2f / (far - near), -(far + near) / (far - near),
                0f, 0f, 0f, 1f);
    }

    public static Matrix4 perspective(float fovx, float fovy, float near, float far) {
        return new Matrix4((float) (1f / Math.tan(fovx * 0.5f)), 0f, 0f, 0f,
                0f, (float) (2f / Math.tan(fovy * 0.5f)), 0f, 0f,
                0f, 0f, -(far + near) / (far - near), -(2f * far * near) / (far - near),
                0f, 0f, -1f, 0f);
    }

    public Matrix4 set(int i, int j, float val) {
        content[i][j] = val;
        return this;
    }

    public float get(int i, int j) {
        return content[i][j];
    }

    public Matrix4 add(Matrix4 m) {
        Matrix4 out = new Matrix4();
        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < 4; j++) {
                out.content[i][j] = this.content[i][j] + m.content[i][j];
            }
        }
        return out;
    }

    public Matrix4 sub(Matrix4 m) {
        Matrix4 out = new Matrix4();
        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < 4; j++) {
                out.content[i][j] = this.content[i][j] - m.content[i][j];
            }
        }
        return out;
    }

    public Matrix4 mul(Matrix4 m) {
        Matrix4 out = new Matrix4();
        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < 4; j++) {
                out.content[i][j] = this.content[i][0] * m.content[0][j] +
                                    this.content[i][1] * m.content[1][j] +
                                    this.content[i][2] * m.content[2][j] +
                                    this.content[i][3] * m.content[3][j];
            }
        }
        return out;
    }

    public Matrix4 mul(float scalar) {
        Matrix4 out = new Matrix4();
        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < 4; j++) {
                out.content[i][j] = this.content[i][j] * scalar;
            }
        }
        return out;
    }

    public Vector4 mul(Vector4 v) {
        Vector4 out = new Vector4();
        out.x = content[0][0] * v.x + content[0][1] * v.y + content[0][2] * v.z + content[0][3] * v.w;
        out.y = content[1][0] * v.x + content[1][1] * v.y + content[1][2] * v.z + content[1][3] * v.w;
        out.z = content[2][0] * v.x + content[2][1] * v.y + content[2][2] * v.z + content[2][3] * v.w;
        out.w = content[3][0] * v.x + content[3][1] * v.y + content[3][2] * v.z + content[3][3] * v.w;
        return out;
    }

    // Laplace expansion
    public float determinant() {
        float determinant = 0f;

        for(int i = 0; i < 4; i++) {
            float coDet = cofactor(i, 0).determinant();
            int sign = i % 2 == 0 ? 1 : -1;
            determinant += sign * (content[i][0] * coDet);
        }

        return determinant;
    }

    public Matrix3 cofactor(int i, int j) {
        Matrix3 m = new Matrix3();
        for(int row = 0; row < 4; row++) {
            for(int col = 0; col < 4; col++) {
                if(row == i || col == j)
                    continue;
                m.set(row > i ? row - 1 : row, col > j ? col - 1 : col, content[row][col]);
            }
        }
        return m;
    }

    public Matrix4 adjoint() {
        Matrix4 m = new Matrix4();
        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < 4; j++) {
                Matrix3 c = cofactor(i, j);
                int sign = (i + j) % 2 == 0 ? 1 : -1;
                m.set(i, j, sign * c.determinant());
            }
        }
        return m.transpose();
    }

    public Matrix4 inverse() {
        float determinant = determinant();
        if(determinant == 0)
            return null;

        return adjoint().mul(1 / determinant);
    }

    public Matrix4 transpose() {
        Matrix4 out = new Matrix4();
        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < 4; j++) {
                out.set(j, i, content[i][j]);
            }
        }
        return out;
    }

    public Matrix4 translate(Vector3 translation) {
        return mul(new Matrix4().set(0, 3, translation.x).set(1, 3, translation.y)
                .set(2, 3, translation.z));
    }

    public Matrix4 scale(Vector3 scale) {
        return mul(new Matrix4().set(0, 0, scale.x).set(1, 1, scale.y).set(2, 2, scale.z));
    }

    public Matrix4 rotate(Quaternion q) {
        q = q.normalized();
        return mul(new Matrix4(1f - 2f * q.y * q.y - 2f * q.z * q.z, 2f * q.x * q.y - 2f * q.z * q.w,
                2f * q.x * q.z + 2f * q.y * q.w, 0f,
                2f * q.x * q.y + 2f * q.z * q.w, 1f - 2f * q.x * q.x - 2f * q.z * q.z,
                2f * q.y * q.z - 2f * q.x * q.w, 0f,
                2f * q.x * q.z - 2f * q.y * q.w, 2f * q.y * q.z + 2f * q.x * q.w,
                1f - 2f * q.x * q.x - 2f * q.y * q.y, 0f,
                0f, 0f, 0f, 1f));
    }

    public Matrix4 rotateX(float degrees) {
        float radians = (float) Math.toRadians(degrees);
        float sin = (float) Math.sin(radians);
        float cos = (float) Math.cos(radians);
        return mul(new Matrix4().set(1, 1, cos).set(2, 1, -sin).set(1, 2, sin).set(2, 2, cos));
    }

    public Matrix4 rotateY(float degrees) {
        float radians = (float) Math.toRadians(degrees);
        float sin = (float) Math.sin(radians);
        float cos = (float) Math.cos(radians);
        return mul(new Matrix4().set(0, 0, cos).set(2, 0, sin).set(0, 2, -sin).set(2, 2, cos));
    }

    public Matrix4 rotateZ(float degrees) {
        float radians = (float) Math.toRadians(degrees);
        float sin = (float) Math.sin(radians);
        float cos = (float) Math.cos(radians);
        return mul(new Matrix4().set(0, 0, cos).set(1, 0, -sin).set(0, 1, sin).set(1, 1, cos));
    }

    public FloatBuffer toFloatBuffer() {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
        for(int i = 0; i < 16; i++) {
            buffer.put(i, content[i % 4][i / 4]);
        }
        return buffer;
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder("( ");

        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < 4; j++) {
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
        return Arrays.deepEquals(content, ((Matrix4) obj).content);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(content);
    }

}
