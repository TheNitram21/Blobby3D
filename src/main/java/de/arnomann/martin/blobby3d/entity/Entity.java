package de.arnomann.martin.blobby3d.entity;

import de.arnomann.martin.blobby3d.event.EventListener;
import de.arnomann.martin.blobby3d.event.ListenerManager;
import de.arnomann.martin.blobby3d.render.Mesh;
import de.arnomann.martin.blobby3d.render.Renderer;
import de.arnomann.martin.blobby3d.render.Shader;
import de.arnomann.martin.blobby3d.render.texture.ITexture;
import de.arnomann.martin.blobby3d.math.*;

import java.util.Map;

public abstract class Entity implements EventListener {

    private Vector3 position;
    private Quaternion rotation;
    private Shader shader;
    private long id;

    private final Map<String, Object> parameters;

    private boolean disabled = false;

    private static long entityCount = 0;

    public Entity(Vector3 position, Quaternion rotation, Map<String, Object> parameters) {
        this.parameters = parameters;

        this.position = position;
        this.rotation = rotation;
        this.shader = parameters.containsKey("Shader") ? Shader.createFromName((String) parameters.get("Shader")) :
                Renderer.getDefaultShader();

        entityCount++;
        id = entityCount;

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

    public void setPosition(Vector3 position) {
        this.position = position;
    }

    public Vector3 getPosition() {
        return position;
    }

    public void setRotation(Quaternion rotation) {
        this.rotation = rotation;
    }

    public Quaternion getRotation() {
        return rotation;
    }

    public Shader getShader() {
        return shader;
    }

    public long getId() {
        return id;
    }

    public abstract void setMesh(Mesh mesh);
    public abstract Mesh getMesh();

    public abstract void setTexture(ITexture texture);
    public abstract ITexture getTexture();

    public Matrix4 getModelMatrix() {
        return new Matrix4().translate(position).rotate(rotation);
    }

    public final Map<String, Object> getParameters() {
        return parameters;
    }

}
