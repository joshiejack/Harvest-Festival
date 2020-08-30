package joshie.harvest.core.util;

import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;

public class ResourceLoader {
    public static String getJSONResource(ResourceLocation id, String directory) {
        String s = id.getNamespace();
        String s1 = id.getPath();
        InputStream inputstream = null;

        try {
            inputstream = ResourceLoader.class.getResourceAsStream("/assets/" + s + "/" + directory + "/" + s1 + ".json");
            return IOUtils.toString(inputstream);
        } catch (Throwable ignored) {} finally {
            IOUtils.closeQuietly(inputstream);
        }

        return "";
    }
}
