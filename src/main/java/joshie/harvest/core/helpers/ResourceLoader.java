package joshie.harvest.core.helpers;

import com.google.common.io.CharStreams;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.zip.GZIPInputStream;

public class ResourceLoader {
    public static String getJSONResource(ResourceLocation id, String directory) {
        String s = id.getResourceDomain();
        String s1 = id.getResourcePath();
        InputStream inputstream = null;

        try {
            inputstream = ResourceLoader.class.getResourceAsStream("/assets/" + s + "/" + directory + "/" + s1 + ".json");
            return IOUtils.toString(inputstream);
        } catch (Throwable var10) {} finally {
            IOUtils.closeQuietly(inputstream);
        }

        return "";
    }

    public static String getBuildingResource(ResourceLocation id, String directory) {
        String s = id.getResourceDomain();
        String s1 = id.getResourcePath();
        InputStream inputstream = null;
        try {
            inputstream = ResourceLoader.class.getResourceAsStream("/assets/" + s + "/" + directory + "/" + s1 + ".building");
            Reader reader = new InputStreamReader(new GZIPInputStream(inputstream), "UTF-8");
            String ret = CharStreams.toString(reader);
            reader.close();
            return ret;
        } catch (Throwable var10) { var10.printStackTrace();} finally {
            IOUtils.closeQuietly(inputstream);
        }

        return "";
    }
}
