package de.arnomann.martin.blobby3d.entity;

import de.arnomann.martin.blobby3d.render.Model;
import de.arnomann.martin.blobby3d.render.texture.ITexture;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.Map;

public class PointLight extends Entity {

    private Vector3f color;
    private float constant;
    private float linear;
    private float quadratic;

    public PointLight(Vector3f position, Quaternionf rotation, Map<String, Object> parameters) {
        super(position, rotation, parameters);

        color = (Vector3f) parameters.get("Color");
        constant = (float) parameters.get("Constant");
        linear = (float) parameters.get("Linear");
        quadratic = (float) parameters.get("Quadratic");
    }

    public Vector3f getColor() {
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
    public void setModel(Model model) {}

    @Override
    public Model getModel() {
        return null;
    }

    @Override
    public void setTexture(ITexture texture) {}

    @Override
    public ITexture getTexture() {
        return null;
    }

}
