package de.arnomann.martin.blobby3d.core;

import de.arnomann.martin.blobby3d.RunConfigurations;
import de.arnomann.martin.blobby3d.entity.Entity;
import de.arnomann.martin.blobby3d.level.Block;
import de.arnomann.martin.blobby3d.level.Level;
import de.arnomann.martin.blobby3d.logging.Logger;
import de.arnomann.martin.blobby3d.render.Mesh;
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
import java.util.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL46.*;

public class Blobby3D {

    public static final String MAPS_PATH = "maps/";
    public static final String TEXTURES_PATH = "textures/";
    public static final String MODELS_PATH = "models/";
    public static final String SHADERS_PATH = "shaders/";
    public static final String BINARIES_PATH = "bin/";

    private static Window window;
    private static Logger logger;
    private static final Map<String, ITexture> cachedTextures = new HashMap<>();
    private static final Map<String, Mesh> cachedMeshes = new HashMap<>();
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
        logger.debug("Loading entities.json");
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

    public static ITexture getTexture(String name) {
        name += ".png";

        if(cachedTextures.containsKey(name)) {
            return cachedTextures.get(name);
        }

        ITexture texture = loadTexture(name);
        cachedTextures.put(name, texture);
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

    public static Mesh getMesh(String name) {
        name += ".obj";

        if(cachedMeshes.containsKey(name)) {
            return cachedMeshes.get(name);
        }

        Mesh mesh = loadMesh(name);
        cachedMeshes.put(name, mesh);
        return mesh;
    }

    private static Mesh loadMesh(String filename) {
        logger.debug("Loading mesh \"" + filename + "\"");

        filename = MODELS_PATH + filename;
        String[] content = readFile(filename).split("\n");

        List<Vector3> verticesIn = new ArrayList<>();
        List<Vector3> normalsIn = new ArrayList<>();
        List<Vector2> textureCoordinatesIn = new ArrayList<>();
        List<String[]> faces = new ArrayList<>();

        for(String line : content) {
            String[] lineSplit = line.split("#")[0].split(" ");
            switch(lineSplit[0]) {
                case "v":
                    verticesIn.add(new Vector3(Float.parseFloat(lineSplit[1]), Float.parseFloat(lineSplit[2]),
                            Float.parseFloat(lineSplit[3])));
                    break;
                case "vn":
                    normalsIn.add(new Vector3(Float.parseFloat(lineSplit[1]), Float.parseFloat(lineSplit[2]),
                            Float.parseFloat(lineSplit[3])));
                    break;
                case "vt":
                    textureCoordinatesIn.add(new Vector2(Float.parseFloat(lineSplit[1]), Float.parseFloat(lineSplit[2])));
                    break;
                case "f":
                    faces.add(Arrays.copyOfRange(lineSplit, 1, 4));
                    break;
            }
        }

        List<Vector3> vertices = new ArrayList<>();
        List<Vector3> normals = new ArrayList<>();
        List<Vector2> textureCoordinates = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();
        Map<String, Integer> seenVertices = new HashMap<>();
        for(String[] face : faces) {
            for(String v : face) {
                if(seenVertices.containsKey(v)) {
                    indices.add(seenVertices.get(v));
                } else {
                    String[] vertexSplit = v.split("/");
                    vertices.add(verticesIn.get(Integer.parseInt(vertexSplit[0]) - 1));
                    if(vertexSplit.length == 2) {
                        textureCoordinates.add(textureCoordinatesIn.get(Integer.parseInt(vertexSplit[1]) - 1));
                    } else if(vertexSplit.length == 3) {
                        if(!vertexSplit[1].isEmpty()) {
                            textureCoordinates.add(textureCoordinatesIn.get(Integer.parseInt(vertexSplit[1]) - 1));
                        }
                        normals.add(normalsIn.get(Integer.parseInt(vertexSplit[2]) - 1));
                    }

                    int index = seenVertices.size();
                    seenVertices.put(v, index);
                    indices.add(index);
                }
            }
        }

        float[] verticesOut = new float[vertices.size() * 3];
        for(int i = 0; i < verticesOut.length; i += 3) {
            Vector3 vertex = vertices.get(i / 3);
            verticesOut[i] = vertex.x;
            verticesOut[i + 1] = vertex.y;
            verticesOut[i + 2] = vertex.z;
        }
        float[] textureCoordinatesOut = new float[textureCoordinates.size() * 2];
        for(int i = 0; i < textureCoordinatesOut.length; i += 2) {
            Vector2 texCoord = textureCoordinates.get(i / 2);
            textureCoordinatesOut[i] = texCoord.x;
            textureCoordinatesOut[i + 1] = texCoord.y;
        }
        float[] normalsOut = new float[normals.size() * 3];
        for(int i = 0; i < normalsOut.length; i += 3) {
            Vector3 normal = normals.get(i / 3);
            normalsOut[i] = normal.x;
            normalsOut[i + 1] = normal.y;
            normalsOut[i + 2] = normal.z;
        }
        int[] indicesOut = new int[indices.size()];
        for(int i = 0; i < indices.size(); i++) {
            indicesOut[i] = indices.get(i);
        }

        return new Mesh(verticesOut, textureCoordinatesOut, normalsOut, indicesOut);
    }

    public static void resetCachedMeshes() {
        cachedMeshes.clear();
    }

    public static Entity instantiateEntity(String className, Vector3 position, Quaternion rotation,
                                           Vector3 scale, Map<String, Object> parameters)
            throws ReflectiveOperationException {
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
        return (Entity) entityClass.getConstructor(Vector3.class, Quaternion.class, Vector3.class, Map.class)
                .newInstance(position, rotation, scale, parameters);
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
