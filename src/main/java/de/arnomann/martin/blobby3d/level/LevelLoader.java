package de.arnomann.martin.blobby3d.level;

import de.arnomann.martin.blobby3d.core.Blobby3D;
import de.arnomann.martin.blobby3d.entity.Entity;
import de.arnomann.martin.blobby3d.logging.Logger;
import de.arnomann.martin.blobby3d.render.texture.ITexture;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static org.lwjgl.opengl.GL33.*;

public class LevelLoader {

    private static final Logger logger = new Logger();

    private LevelLoader() {}

    public static void loadLevel(String name, Consumer<Level> onDone) {
        String fileName = Blobby3D.MAPS_PATH + name + ".json";
        logger.info("Loading level \"" + fileName + "\"...");

        JSONObject json = new JSONObject(Blobby3D.readFile(fileName));

        JSONObject ambientLightJSON = json.getJSONObject("AmbientLight");
        Vector3f ambientLightColor = new Vector3f(ambientLightJSON.getFloat("R"), ambientLightJSON.getFloat("G"),
                ambientLightJSON.getFloat("B"));

        List<Block> blocks = new ArrayList<>();
        for(Object blockObject : json.getJSONArray("Blocks")) {
            JSONObject blockJSON = (JSONObject) blockObject;

            Vector3f position = loadVector(blockJSON.getJSONObject("Position"));
            Quaternionf rotation = loadQuaternion(blockJSON.getJSONObject("Rotation"));
            Vector3f dimensions = loadVector(blockJSON.getJSONObject("Dimensions"));
            ITexture texture = Blobby3D.getTexture(blockJSON.getString("Texture"));

            blocks.add(new Block(position, rotation, dimensions, texture));
        }

        List<Entity> entities = new ArrayList<>();
        for(Object entityObject : json.getJSONArray("Entities")) {
            JSONObject entityJSON = (JSONObject) entityObject;
            String className = entityJSON.getString("ClassName");

            Map<String, String> parametersData = Blobby3D.getEntityData().get(className);

            Vector3f position = loadVector(entityJSON.getJSONObject("Position"));
            Quaternionf rotation = loadQuaternion(entityJSON.getJSONObject("Rotation"));

            Map<String, Object> entityParameters = new HashMap<>();
            JSONObject parametersJSON = entityJSON.getJSONObject("Parameters");
            for(String parameterName : parametersJSON.keySet()) {
                String parameterType = parametersData.get(parameterName);
                switch(parameterType.toLowerCase()) {
                    case "string":
                        entityParameters.put(parameterName, parametersJSON.getString(parameterName));
                        break;
                    case "int":
                        entityParameters.put(parameterName, parametersJSON.getInt(parameterName));
                        break;
                    case "float":
                        entityParameters.put(parameterName, parametersJSON.getFloat(parameterName));
                        break;
                    case "vector3":
                        JSONObject vectorJSON = parametersJSON.getJSONObject(parameterName);
                        entityParameters.put(parameterName, loadVector(vectorJSON));
                        break;
                    case "color_rgb":
                        JSONObject colorRGBJSON = parametersJSON.getJSONObject(parameterName);
                        entityParameters.put(parameterName, loadRGB(colorRGBJSON));
                        break;
                    case "color_rgba":
                        JSONObject colorRGBAJSON = parametersJSON.getJSONObject(parameterName);
                        entityParameters.put(parameterName, loadRGBA(colorRGBAJSON));
                        break;
                }
            }

            try {
                entities.add(Blobby3D.instantiateEntity(className, position, rotation, entityParameters));
            } catch (ReflectiveOperationException e) {
                logger.error("Couldn't load level \"" + fileName + "\".", e);
                return;
            }
        }

        Level level = new Level(ambientLightColor, blocks, entities);
        onDone.accept(level);
    }

    private static Vector3f loadVector(JSONObject json) {
        return new Vector3f(json.getFloat("X"), json.getFloat("Y"), json.getFloat("Z"));
    }

    private static Quaternionf loadQuaternion(JSONObject json) {
        return new Quaternionf().rotateXYZ(json.getFloat("X"), json.getFloat("Y"), json.getFloat("Z"));
    }

    private static Vector3f loadRGB(JSONObject json) {
        return new Vector3f(json.getFloat("R"), json.getFloat("G"), json.getFloat("B"));
    }

    private static Vector4f loadRGBA(JSONObject json) {
        return new Vector4f(json.getFloat("R"), json.getFloat("G"), json.getFloat("B"),
                json.getFloat("A"));
    }

}
