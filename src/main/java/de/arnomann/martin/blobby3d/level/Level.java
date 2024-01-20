package de.arnomann.martin.blobby3d.level;

import de.arnomann.martin.blobby3d.entity.Entity;
import org.joml.Vector3f;

import java.util.List;

public class Level {

    private Vector3f ambientLightColor;
    private List<Block> blocks;
    private List<Entity> entities;

    public Level(Vector3f ambientLightColor, List<Block> blocks, List<Entity> entities) {
        this.ambientLightColor = ambientLightColor;
        this.blocks = blocks;
        this.entities = entities;
    }

    public Vector3f getAmbientLightColor() {
        return ambientLightColor;
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    public Block getBlockById(long id) {
        for(Block block : blocks) {
            if(block.getId() == id)
                return block;
        }
        return null;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public Entity getEntityById(long id) {
        for(Entity entity : entities) {
            if(entity.getId() == id)
                return entity;
        }
        return null;
    }

}
