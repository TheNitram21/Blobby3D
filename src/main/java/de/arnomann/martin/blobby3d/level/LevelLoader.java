package de.arnomann.martin.blobby3d.level;

import de.arnomann.martin.blobby3d.core.Blobby3D;
import de.arnomann.martin.blobby3d.entity.Entity;
import de.arnomann.martin.blobby3d.logging.Logger;
import de.arnomann.martin.blobby3d.render.texture.ITexture;
import de.arnomann.martin.blobby3d.math.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class LevelLoader {

    private static final Logger logger = new Logger();

    private LevelLoader() {}

    public static void loadLevel(String name, Consumer<Level> onDone) {
        String fileName = Blobby3D.MAPS_PATH + name + ".json";
        logger.info("Loading level \"" + fileName + "\"...");

        try {
            JSONObject json = new JSONObject(Blobby3D.readFile(fileName));

            Vector3 ambientLightColor = loadRGB(json.getJSONObject("AmbientLight"));

            List<Block> blocks = new ArrayList<>();
            for(Object blockObject : json.getJSONArray("Blocks")) {
                JSONObject blockJSON = (JSONObject) blockObject;

                Vector3 position = loadVector(blockJSON.getJSONObject("Position"));
                Quaternion rotation = loadQuaternion(blockJSON.getJSONObject("Rotation"));
                Vector3 dimensions = loadVector(blockJSON.getJSONObject("Dimensions"));
                ITexture texture = Blobby3D.getTexture(blockJSON.getString("Texture"));

                JSONArray facesArray = blockJSON.getJSONArray("Faces");
                boolean[] faces = new boolean[facesArray.length()];
                for(int i = 0; i < facesArray.length(); i++) {
                    faces[i] = facesArray.getBoolean(i);
                }

                blocks.add(new Block(position, rotation, dimensions, texture, faces));
            }

            List<Entity> entities = new ArrayList<>();
            for(Object entityObject : json.getJSONArray("Entities")) {
                JSONObject entityJSON = (JSONObject) entityObject;
                String className = entityJSON.getString("ClassName");

                Map<String, String> parametersData = Blobby3D.getEntityData().get(className);

                Vector3 position = loadVector(entityJSON.getJSONObject("Position"));
                Quaternion rotation = loadQuaternion(entityJSON.getJSONObject("Rotation"));
                Vector3 scale = loadVector(entityJSON.getJSONObject("Scale"));

                Map<String, Object> entityParameters = new HashMap<>();
                JSONObject parametersJSON = entityJSON.getJSONObject("Parameters");
                for(String parameterName : parametersJSON.keySet()) {
                    String parameterType = parametersData.get(parameterName);
                    switch (parameterType.toLowerCase()) {
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
                    entities.add(Blobby3D.instantiateEntity(className, position, rotation, scale, entityParameters));
                } catch(ReflectiveOperationException e) {
                    logger.fatal("Couldn't load level \"" + fileName + "\".", e);
                    return;
                }
            }

            Level level = new Level(ambientLightColor, blocks, entities);
            onDone.accept(level);
        } catch(JSONException e) {
            logger.fatal("Couldn't load level \"" + fileName + "\".", e);
        }
    }

    private static Vector3 loadVector(JSONObject json) {
        return new Vector3(json.getFloat("X"), json.getFloat("Y"), json.getFloat("Z"));
    }

    private static Quaternion loadQuaternion(JSONObject json) {
        return Quaternion.fromEulerAngles(json.getFloat("X"), json.getFloat("Y"), json.getFloat("Z"));
    }

    private static Vector3 loadRGB(JSONObject json) {
        return new Vector3(json.getFloat("R"), json.getFloat("G"), json.getFloat("B"));
    }

    private static Vector4 loadRGBA(JSONObject json) {
        return new Vector4(json.getFloat("R"), json.getFloat("G"), json.getFloat("B"),
                json.getFloat("A"));
    }

}
