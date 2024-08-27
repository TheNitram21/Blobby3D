package de.arnomann.martin.blobby3d.core;

import de.arnomann.martin.blobby3d.RunConfigurations;
import de.arnomann.martin.blobby3d.entity.Entity;
import de.arnomann.martin.blobby3d.level.Block;
import de.arnomann.martin.blobby3d.level.Level;
import de.arnomann.martin.blobby3d.logging.Logger;
import de.arnomann.martin.blobby3d.render.RenderAPI;
import de.arnomann.martin.blobby3d.render.Renderer;
import de.arnomann.martin.blobby3d.render.texture.ITexture;
import de.arnomann.martin.blobby3d.render.texture.Texture;
import de.arnomann.martin.blobby3d.math.*;
import org.json.JSONObject;
import org.lwjgl.BufferUtils;

import java.io.*;
import java.nio.DoubleBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL46.*;

public class Blobby3D {

    public static final String MAPS_PATH = "maps/";
    public static final String TEXTURES_PATH = "textures/";
    public static final String SHADERS_PATH = "shaders/";
    public static final String BINARIES_PATH = "bin/";

    private static Window window;
    private static Logger logger;
    private static final Map<String, ITexture> cachedTextures = new HashMap<>();
    private static final Map<String, Map<String, String>> entityData = new HashMap<>();

    private static boolean cursorVisible;

    private static Level level;

    private static RenderAPI currentRenderAPI;

    private Blobby3D() {}

    public static void run(RunConfigurations runConfig) {
        logger = new Logger();
        loadEntities(BINARIES_PATH + "entities.json");

        if(!glfwInit()) {
            logger.error("An error occurred whilst trying to initialize GLFW.");
            return;
        }

        currentRenderAPI = runConfig.renderAPI;
        if(currentRenderAPI == RenderAPI.VULKAN) {
            logger.error("Vulkan is not supported yet!");
            return;
        }

        window = new Window(runConfig);
        if(window.getId() == 0) {
            return;
        }

        Block.initialize();
        Renderer.initialize();
        Input.initialize();

        window.start();
    }

    public static void stop() {
        glfwSetWindowShouldClose(window.getId(), true);
    }

    public static String readFile(String path) {
        StringBuilder content = new StringBuilder();

        try(BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return content.toString();
    }

    public static void loadEntities(String entitiesPath) {
        JSONObject json = new JSONObject(readFile(entitiesPath));

        for(Object entityObject : json.getJSONArray("Entities")) {
            JSONObject entityJSON = (JSONObject) entityObject;
            String className = entityJSON.getString("ClassName");

            Map<String, String> parametersMap = new HashMap<>();
            JSONObject parametersJSON = entityJSON.getJSONObject("Parameters");
            for(String parameterName : parametersJSON.keySet()) {
                parametersMap.put(parameterName, parametersJSON.getString(parameterName));
            }

            entityData.put(className, parametersMap);
        }
    }

    public static Map<String, Map<String, String>> getEntityData() {
        return entityData;
    }

    public static ITexture getTexture(String filename) {
        filename += ".png";

        if(cachedTextures.containsKey(filename)) {
            return cachedTextures.get(filename);
        }

        ITexture texture = loadTexture(filename);
        cachedTextures.put(filename, texture);
        return texture;
    }

    private static ITexture loadTexture(String filename) {
        filename = TEXTURES_PATH + filename;
        if(!Files.exists(Path.of(filename)))
            return null;

        return new Texture(filename);
    }

    public static void resetCachedTextures() {
        cachedTextures.clear();
    }

    public static Entity instantiateEntity(String className, Vector3 position, Quaternion rotation,
                                           Map<String, Object> parameters) throws ReflectiveOperationException {
        JSONObject entitiesJSON = new JSONObject(readFile(BINARIES_PATH + "entities.json"));
        String internalClassName = null;

        for(Object entityObject : entitiesJSON.getJSONArray("Entities")) {
            JSONObject entityJSON = (JSONObject) entityObject;

            if(entityJSON.getString("ClassName").equals(className))
                internalClassName = entityJSON.getString("InternalClassName");
        }

        if(internalClassName == null)
            return null;

        Class<?> entityClass = Class.forName(internalClassName);
        return (Entity) entityClass.getConstructor(Vector3.class, Quaternion.class, Map.class)
                .newInstance(position, rotation, parameters);
    }

    public static void setCursorVisible(boolean visible) {
        cursorVisible = visible;
        glfwSetInputMode(window.getId(), GLFW_CURSOR, visible ? GLFW_CURSOR_NORMAL : GLFW_CURSOR_DISABLED);
    }

    public static boolean getCursorVisible() {
        return cursorVisible;
    }

    public static void setCursorPosition(Vector2 position) {
        glfwSetCursorPos(window.getId(), position.x, position.y);
    }

    public static Vector2 getCursorPosition() {
        DoubleBuffer xPos = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer yPos = BufferUtils.createDoubleBuffer(1);
        glfwGetCursorPos(window.getId(), xPos, yPos);
        return new Vector2((float) xPos.get(0), (float) yPos.get(0));
    }

    public static String getOpenGLError() {
        if(currentRenderAPI != RenderAPI.OPENGL)
            return null;
        return glGetString(glGetError());
    }

    public static String getVulkanError() {
        if(currentRenderAPI != RenderAPI.VULKAN)
            return null;
        return null;
    }

    public static void setLevel(Level level) {
        if(Blobby3D.level != null)
            Blobby3D.level.getEntities().forEach(Entity::disable);

        Blobby3D.level = level;
    }

    public static Level getLevel() {
        return level;
    }

    public static Window getWindow() {
        return window;
    }

    public static Logger getLogger() {
        return logger;
    }

    public static RenderAPI getRenderAPI() {
        return currentRenderAPI;
    }

}
