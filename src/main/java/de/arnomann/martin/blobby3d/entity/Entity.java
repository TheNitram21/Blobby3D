package de.arnomann.martin.blobby3d.entity;

import de.arnomann.martin.blobby3d.event.EventListener;
import de.arnomann.martin.blobby3d.event.ListenerManager;
import de.arnomann.martin.blobby3d.render.Model;
import de.arnomann.martin.blobby3d.render.texture.ITexture;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.Map;

public abstract class Entity implements EventListener {

    private Vector3f position;
    private Quaternionf rotation;

    private final Map<String, Object> parameters;

    private boolean disabled = false;

    public Entity(Vector3f position, Quaternionf rotation, Map<String, Object> parameters) {
        this.parameters = parameters;

        this.position = position;
        this.rotation = rotation;

        ListenerManager.registerEventListener(this);
    }

    public void disable() {
        if(disabled)
            return;

        disabled = true;
        ListenerManager.removeEventListener(this);
    }

    public void enable() {
        if(!disabled)
            return;

        disabled = false;
        ListenerManager.registerEventListener(this);
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setRotation(Quaternionf rotation) {
        this.rotation = rotation;
    }

    public Quaternionf getRotation() {
        return rotation;
    }

    public abstract void setModel(Model model);
    public abstract Model getModel();

    public abstract void setTexture(ITexture texture);
    public abstract ITexture getTexture();

    public Matrix4f getModelMatrix() {
        return new Matrix4f().translate(position).rotate(rotation);
    }

    public final Map<String, Object> getParameters() {
        return parameters;
    }

}
