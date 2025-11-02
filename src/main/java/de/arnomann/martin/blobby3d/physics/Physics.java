package de.arnomann.martin.blobby3d.physics;

import de.arnomann.martin.blobby3d.core.Blobby3D;
import de.arnomann.martin.blobby3d.entity.Entity;
import de.arnomann.martin.blobby3d.level.Block;
import de.arnomann.martin.blobby3d.math.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Physics {

    private Physics() {}

    public static boolean aabb(Vector3 centerA, Vector3 sizeA, Vector3 centerB, Vector3 sizeB) {
        sizeA = sizeA.div(2);
        sizeB = sizeB.div(2);
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
        public Collider collider;

        public RayResult(boolean hit, Vector3 hitPoint, float hitDistance, Collider collider) {
            this.hit = hit;
            this.hitPoint = hitPoint;
            this.hitDistance = hitDistance;
            this.collider = collider;
        }
    }

    public static RayResult raycastBlocks(Vector3 rayStart, Vector3 rayDirection) {
        rayDirection = rayDirection.normalized();

        for(Block block : Blobby3D.getLevel().getBlocks()) {
            Matrix4 inverseModelMatrix = block.getModelMatrix().inverse();
            Vector3 localRayStart = new Vector3(inverseModelMatrix.mul(new Vector4(rayStart, 1f)));
            Vector3 localRayDirection = new Vector3(inverseModelMatrix.mul(new Vector4(rayDirection, 0f)));

            RayResult result = rayAABBIntersect(localRayStart, localRayDirection, new Vector3(-0.5f, -0.5f, -0.5f),
                    new Vector3(0.5f, 0.5f, 0.5f));
            if(result.hit) {
                result.hitPoint = rayStart.add(rayDirection.mul(result.hitDistance));
                result.collider = block;
                return result;
            }
        }

        return new RayResult(false, null, 0f, null);
    }

    private static RayResult rayAABBIntersect(Vector3 rayStart, Vector3 rayDirection, Vector3 min, Vector3 max) {
        float tMin = (min.x - rayStart.x) / rayDirection.x;
        float tMax = (max.x - rayStart.x) / rayDirection.x;
        if(tMin > tMax) {
            float tmp = tMin;
            tMin = tMax;
            tMax = tmp;
        }

        float tyMin = (min.y - rayStart.y) / rayDirection.y;
        float tyMax = (max.y - rayStart.y) / rayDirection.y;
        if(tyMin > tyMax) {
            float tmp = tyMin;
            tyMin = tyMax;
            tyMax = tmp;
        }

        if(tMin > tyMax || tyMin > tMax)
            return new RayResult(false, null, 0f, null);
        if(tyMin > tMin)
            tMin = tyMin;
        if(tyMax < tMax)
            tMax = tyMax;

        float tzMin = (min.z - rayStart.z) / rayDirection.z;
        float tzMax = (max.z - rayStart.z) / rayDirection.z;
        if(tzMin > tzMax) {
            float tmp = tzMin;
            tzMin = tzMax;
            tzMax = tmp;
        }

        if(tMin > tzMax || tzMin > tMax)
            return new RayResult(false, null, 0f, null);
        if(tzMin > tMin)
            tMin = tzMin;
        if(tzMax < tMax)
            tMax = tzMax;

        if(tMax < 0)
            return new RayResult(false, null, 0f, null);
        return new RayResult(true, null, tMin, null); // hitPoint will be calculated and collider will be assigned by the caller!
    }

    private static RayResult rayTriangleIntersect(Vector3 rayStart, Vector3 rayDirection,
                                                Vector3 pointA, Vector3 pointB, Vector3 pointC) {
        float epsilon = 0.00000001f;
        Vector3 edgeA = pointB.sub(pointA);
        Vector3 edgeB = pointC.sub(pointA);

        Vector3 h = rayDirection.cross(edgeB);
        float a = edgeA.dot(h);
        if(-epsilon < a && a < epsilon)
            return new RayResult(false, null, 0f, null);

        float f = 1f / a;
        Vector3 s = rayStart.sub(pointA);
        float u = f * s.dot(h);
        if(u < 0f || u > 1f)
            return new RayResult(false, null, 0f, null);

        Vector3 q = s.cross(edgeA);
        float v = f * rayDirection.dot(q);
        if(v < 0f || u + v > 1f)
            return new RayResult(false, null, 0f, null);

        float t = f * edgeB.dot(q);
        if(t > epsilon)
            return new RayResult(true, rayStart.add(rayDirection.mul(t)), t, null); // collider will be assigned by the caller!
        return new RayResult(false, null, 0f, null);
    }

    public static boolean checkForWorldCollision(Collider collider) {
        float colliderRadius = collider.getCollisionMesh().getRadius() * collider.getScale().largest();
        Vector3 colliderPosition = collider.getPosition();
        for(Block block : Blobby3D.getLevel().getBlocks()) {
            float maxDistance = block.getCollisionMesh().getRadius() * block.getScale().largest() + colliderRadius;
            if(block.getPosition().sub(colliderPosition).lengthSquared() > maxDistance * maxDistance)
                continue;

            if(colliding(collider, block))
                return true;
        }
        return false;
    }

    // Implementation of SAT: Only for convex meshes!
    public static boolean colliding(Collider colliderA, Collider colliderB) {
        float radiusA = colliderA.getCollisionMesh().getRadius() * Math.max(colliderA.getScale().x, Math.max(
                colliderA.getScale().y, colliderA.getScale().z));
        float radiusB = colliderB.getCollisionMesh().getRadius() * Math.max(colliderB.getScale().x, Math.max(
                colliderB.getScale().y, colliderB.getScale().z));
        if(colliderA.getPosition().sub(colliderB.getPosition()).lengthSquared() > (radiusA + radiusB) * (radiusA + radiusB))
            return false;

        Set<Vector3> axes = new HashSet<>(colliderA.getCollisionMesh().getNormals());
        axes.addAll(colliderB.getCollisionMesh().getNormals());
        axes.addAll(colliderA.getCollisionMesh().getEdges());
        axes.addAll(colliderB.getCollisionMesh().getEdges());

        for(Vector3 axis : axes) {
            float minA = Float.MAX_VALUE, maxA = Float.MIN_VALUE;
            float minB = Float.MAX_VALUE, maxB = Float.MIN_VALUE;
            axis = axis.normalized();

            for(Vector3 vertex : colliderA.getCollisionMesh().getVertices()) {
                float projected = vertex.add(colliderA.getPosition()).dot(axis);
                if(projected < minA)
                    minA = projected;
                if(projected > maxA)
                    maxA = projected;
            }
            for(Vector3 vertex : colliderB.getCollisionMesh().getVertices()) {
                float projected = vertex.add(colliderB.getPosition()).dot(axis);
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
