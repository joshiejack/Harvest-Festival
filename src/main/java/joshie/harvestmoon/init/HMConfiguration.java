package joshie.harvestmoon.init;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Method;

import joshie.harvestmoon.HarvestMoon;
import joshie.harvestmoon.core.config.Mappings;
import joshie.harvestmoon.core.config.Vanilla;
import joshie.harvestmoon.core.helpers.generic.ConfigHelper;
import joshie.harvestmoon.core.lib.HMModInfo;
import joshie.harvestmoon.crops.Crop;
import net.minecraftforge.common.config.Configuration;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Level;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class HMConfiguration {
    public static final int PACKET_DISTANCE = 172;
    public static Mappings mappings;
    public static Vanilla vanilla;

    public static void init() {
        initConfig("General");
        initConfig("Animals");
        initConfig("Calendar");
        initConfig("Client");
        initConfig("Cooking");
        initConfig("NPC");
        initConfig("Shops");
        initConfig("Tools");
    }

    public static void initConfig(String name) {
        Configuration config = new Configuration(new File(HarvestMoon.root, name.replaceAll("(.)([A-Z])", "$1-$2").toLowerCase() + ".cfg"));
        try {
            config.load();
            ConfigHelper.setConfig(config);
            Class clazz = Class.forName(HMModInfo.JAVAPATH + "core.config." + name);
            Method method = clazz.getMethod("init", Configuration.class);
            method.invoke(null, config);
        } catch (Exception e) {
            HarvestMoon.logger.log(Level.ERROR, HMModInfo.MODNAME + " failed to load in it's " + name + " config");
            e.printStackTrace();
        } finally {
            config.save();
        }
    }

    /** Creates the configuration file for the ASM Configs **/
    public static void initASM(Gson gson) {
        File file = new File("config/" + HMModInfo.MODPATH + "/vanilla.json");
        if (!file.exists()) {
            try {
                HMConfiguration.vanilla = new Vanilla();
                Writer writer = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
                writer.write(gson.toJson(HMConfiguration.vanilla));
                writer.close(); //Write the default json to file
            } catch (Exception e) {}
        } else {
            try {
                HMConfiguration.vanilla = gson.fromJson(FileUtils.readFileToString(file), Vanilla.class);
            } catch (Exception e) {}
        }
    }

    private static Mappings getMappings(Gson gson) {
        File file = new File("config/" + HMModInfo.MODPATH + "/crop_mappings.json");
        if (!file.exists()) {
            return new Mappings();
        } else {
            try {
                return gson.fromJson(FileUtils.readFileToString(file), Mappings.class);
            } catch (Exception e) {}
        }

        return new Mappings();
    }

    /** Creates the crops json file **/
    public static int addCropMapping(Crop crop) {
        System.out.println("CROP" + crop.getUnlocalizedName());
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().excludeFieldsWithoutExposeAnnotation().create();
        if (HMConfiguration.mappings == null) {
            /** If mappings is null, load it from the config **/
            HMConfiguration.mappings = getMappings(gson);
        }

        System.out.println("About to call GeTID for " + crop.getUnlocalizedName());
        Integer id = HMConfiguration.mappings.getID(crop); //The crops ID

        //Now that we have the ID for this crop type. We should update the mappings json
        File file = new File("config/" + HMModInfo.MODPATH + "/crop_mappings.json");
        try {
            Writer writer = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
            writer.write(gson.toJson(HMConfiguration.mappings));
            writer.close(); //Write the default json to file
        } catch (Exception e) {}

        return id;
    }

    public static void clear() {
        if (mappings != null) {
            mappings.clear();
        }
    }
}
