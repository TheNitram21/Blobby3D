package de.arnomann.martin.blobby3d.render.texture;

import de.arnomann.martin.blobby3d.core.Blobby3D;
import org.json.JSONObject;

public class TextureData {

    public enum StretchMode {
        FIT,
        REPEAT,
        CLAMP
    }

    public boolean linearFiltering = true;
    public StretchMode stretch;

    public static TextureData parseTextureData(String filename) {
        JSONObject textureDataJSON;
        try {
            textureDataJSON = new JSONObject(Blobby3D.readFile(filename + ".json"));
        } catch(RuntimeException e) {
            Blobby3D.getLogger().error("Couldn't load texture \"" + filename + "\": Couldn't load texture data!",
                    e);
            return null;
        }

        TextureData data = new TextureData();
        String illegalPropertyError = "Couldn't load texture data for file \"" + filename +
                "\": Illegal value for property ";
        String propertyMissingError = "Couldn't load texture data for file \"" + filename +
                "\": Missing property ";

        if(textureDataJSON.has("FilterMode")) {
            if(textureDataJSON.getString("FilterMode").equalsIgnoreCase("LINEAR")) {
                data.linearFiltering = true;
            } else if(textureDataJSON.getString("FilterMode").equalsIgnoreCase("NEAREST")) {
                data.linearFiltering = false;
            } else {
                Blobby3D.getLogger().error(illegalPropertyError + "FilterMode");
                return null;
            }
        } else {
            Blobby3D.getLogger().error(propertyMissingError + "FilterMode");
            return null;
        }

        if(textureDataJSON.has("Stretch")) {
            if(textureDataJSON.getString("Stretch").equalsIgnoreCase("FIT")) {
                data.stretch = StretchMode.FIT;
            } else if(textureDataJSON.getString("Stretch").equalsIgnoreCase("REPEAT")) {
                data.stretch = StretchMode.REPEAT;
            } else if(textureDataJSON.getString("Stretch").equalsIgnoreCase("CLAMP")) {
                data.stretch = StretchMode.CLAMP;
            } else {
                Blobby3D.getLogger().error(illegalPropertyError + "Stretch");
                return null;
            }
        } else {
            Blobby3D.getLogger().error(propertyMissingError + "Stretch");
            return null;
        }

        return data;
    }

}
