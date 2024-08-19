package de.arnomann.martin.blobby3d.physics;

import de.arnomann.martin.blobby3d.math.*;

import java.util.HashSet;
import java.util.Set;

public class Physics {

    private Physics() {}

    public static boolean aabb(Vector3 centerA, Vector3 sizeA, Vector3 centerB, Vector3 sizeB) {
        sizeA.div(2);
        sizeB.div(2);
        Vector3 minA = centerA.sub(sizeA);
        Vector3 maxA = centerA.add(sizeA);
        Vector3 minB = centerB.sub(sizeB);
        Vector3 maxB = centerB.add(sizeB);

        return minA.x <= maxB.x &&
               maxA.x >= minB.x &&
               minA.y <= maxB.y &&
               maxA.y >= minB.y &&
               minA.z <= maxB.z &&
               maxA.z >= minB.z;
    }

    public static class RayResult {
        public boolean hit;
        public Vector3 hitPoint;
        public float hitDistance;

        public RayResult(boolean hit, Vector3 hitPoint, float hitDistance) {
            this.hit = hit;
            this.hitPoint = hitPoint;
            this.hitDistance = hitDistance;
        }
    }

    private static RayResult rayTriangleIntersect(Vector3 rayStart, Vector3 rayDirection,
                                                Vector3 pointA, Vector3 pointB, Vector3 pointC) {
        float epsilon = 0.00000001f;
        Vector3 edgeA = pointB.sub(pointA);
        Vector3 edgeB = pointC.sub(pointA);

        Vector3 h = rayDirection.cross(edgeB);
        float a = edgeA.dot(h);
        if(-epsilon < a && a < epsilon)
            return new RayResult(false, null, 0f);

        float f = 1f / a;
        Vector3 s = rayStart.sub(pointA);
        float u = f * s.dot(h);
        if(u < 0f || u > 1f)
            return new RayResult(false, null, 0f);

        Vector3 q = s.cross(edgeA);
        float v = f * rayDirection.dot(q);
        if(v < 0f || u + v > 1f)
            return new RayResult(false, null, 0f);

        float t = f * edgeB.dot(q);
        if(t > epsilon)
            return new RayResult(true, rayStart.add(rayDirection.mul(t)), t);
        return new RayResult(false, null, 0f);
    }

    public static boolean colliding(Collider colliderA, Collider colliderB) {
        float radiusA = colliderA.getCollisionMesh().getRadius() * Math.max(colliderA.getScale().x, Math.max(
                colliderA.getScale().y, colliderA.getScale().z));
        float radiusB = colliderB.getCollisionMesh().getRadius() * Math.max(colliderB.getScale().x, Math.max(
                colliderB.getScale().y, colliderB.getScale().z));
        if(colliderA.getPosition().sub(colliderB.getPosition()).lengthSquared() > (radiusA + radiusB) * (radiusA + radiusB))
            return false;

        Set<Vector3> normals = new HashSet<>(colliderA.getCollisionMesh().getNormals());
        normals.addAll(colliderB.getCollisionMesh().getNormals());

        for(Vector3 normal : normals) {
            float minA = Float.MAX_VALUE, maxA = Float.MIN_VALUE;
            float minB = Float.MAX_VALUE, maxB = Float.MIN_VALUE;
            normal = normal.normalized();

            for(Vector3 vertex : colliderA.getCollisionMesh().getVertices()) {
                float projected = vertex.dot(normal);
                if(projected < minA)
                    minA = projected;
                if(projected > maxA)
                    maxA = projected;
            }
            for(Vector3 vertex : colliderB.getCollisionMesh().getVertices()) {
                float projected = vertex.dot(normal);
                if(projected < minB)
                    minB = projected;
                if(projected > maxB)
                    maxB = projected;
            }

            if(maxA < minB || maxB < minA)
                return false;
        }

        return true;
    }

}
