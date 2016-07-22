package joshie.harvest.core;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import joshie.harvest.HarvestFestival;
import joshie.harvest.core.helpers.generic.ConfigHelper;
import joshie.harvest.core.lib.HFModInfo;
import net.minecraftforge.common.config.Configuration;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Method;

public class HFConfig {
    public static ASM asm;

    public static void preInit() {
        initConfig("General");
       // HFPlugins.loadConfigs();
    }

    public static void initConfig(String name) {
        Configuration config = new Configuration(new File(HarvestFestival.root, name.replaceAll("(.)([A-Z])", "$1-$2").toLowerCase() + ".cfg"));
        try {
            config.load();
            ConfigHelper.setConfig(config);
            Class<?> clazz = Class.forName(HFModInfo.JAVAPATH + name.toLowerCase() + ".HF" + name);
            Method method = clazz.getMethod("configure");
            method.invoke(null);
        } catch (Exception e) {
            HarvestFestival.LOGGER.log(Level.ERROR, HFModInfo.MODNAME + " failed to load in it's " + name + " config");
            e.printStackTrace();
        } finally {
            config.save();
        }
    }

    /** Creates the configuration file for the ASM Configs **/
    public static void initASM(Gson gson) {
        File file = new File("config//" + HFModInfo.MODID + "//vanilla.json");
        if (!file.exists()) {
            try {
                HFConfig.asm = new ASM();
                Writer writer = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
                writer.write(gson.toJson(HFConfig.asm));
                writer.close(); //Write the default json to file
            } catch (Exception ignored) {}
        } else {
            try {
                HFConfig.asm = gson.fromJson(FileUtils.readFileToString(file), ASM.class);
            } catch (Exception ignored) {}
        }
    }

    /* This class is loaded via json for the config */
    public static class ASM {
        @SerializedName("Snow > Enable Override")
        public boolean SNOW_OVERRIDE = true;

        @SerializedName("Rain > Fix Particles when Snowing")
        public boolean RAIN_OVERRIDE = true;
    }
}
