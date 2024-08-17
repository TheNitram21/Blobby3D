package de.arnomann.martin.blobby3d.entity;

import de.arnomann.martin.blobby3d.render.Mesh;
import de.arnomann.martin.blobby3d.render.texture.ITexture;
import de.arnomann.martin.blobby3d.math.*;

import java.util.Map;

public class PointLight extends Entity {

    private Vector3 color;
    private float constant;
    private float linear;
    private float quadratic;

    public PointLight(Vector3 position, Quaternion rotation, Map<String, Object> parameters) {
        super(position, rotation, parameters);

        color = (Vector3) parameters.get("Color");
        constant = (float) parameters.get("Constant");
        linear = (float) parameters.get("Linear");
        quadratic = (float) parameters.get("Quadratic");
    }

    public Vector3 getColor() {
        return color;
    }

    public float getConstant() {
        return constant;
    }

    public float getLinear() {
        return linear;
    }

    public float getQuadratic() {
        return quadratic;
    }

    @Override
    public void setMesh(Mesh mesh) {}

    @Override
    public Mesh getMesh() {
        return null;
    }

    @Override
    public void setTexture(ITexture texture) {}

    @Override
    public ITexture getTexture() {
        return null;
    }

}
