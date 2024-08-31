package de.arnomann.martin.blobby3d.entity;

import de.arnomann.martin.blobby3d.core.Blobby3D;
import de.arnomann.martin.blobby3d.math.Quaternion;
import de.arnomann.martin.blobby3d.math.Vector3;
import de.arnomann.martin.blobby3d.render.Mesh;
import de.arnomann.martin.blobby3d.render.texture.ITexture;

import java.util.Map;

public class DynamicProp extends Entity {

    private Mesh mesh;
    private ITexture texture;

    public DynamicProp(Vector3 position, Quaternion rotation, Vector3 scale,
                       Map<String, Object> parameters) {
        super(position, rotation, scale, parameters);

        mesh = Blobby3D.getMesh((String) parameters.get("Model"));
        texture = Blobby3D.getTexture(Blobby3D.MODELS_PATH + parameters.get("Model"));
    }

    public void setModel(String name) {
        this.mesh = Blobby3D.getMesh(name);
        this.texture = Blobby3D.getTexture(Blobby3D.MODELS_PATH + name);
    }

    @Override
    public void beforeRender() {

    }

    @Override
    public void setMesh(Mesh mesh) {
        this.mesh = mesh;
    }

    @Override
    public Mesh getMesh() {
        return mesh;
    }

    @Override
    public void setTexture(ITexture texture) {
        this.texture = texture;
    }

    @Override
    public ITexture getTexture() {
        return texture;
    }

}
