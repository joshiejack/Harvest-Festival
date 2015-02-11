package joshie.harvestmoon;

import java.io.File;
import java.lang.reflect.Method;

import joshie.harvestmoon.helpers.generic.ConfigHelper;
import joshie.harvestmoon.lib.HMModInfo;
import net.minecraftforge.common.config.Configuration;

import org.apache.logging.log4j.Level;

public class HMConfiguration {
    public static final int PACKET_DISTANCE = 172;
    public static boolean EASY_MODE = false;

    public static void init() {
        initConfig("Calendar");
        initConfig("Client");
        initConfig("Cooking");
        initConfig("Shops");
        initConfig("Tools");
    }

    public static void initConfig(String name) {
        Configuration config = new Configuration(new File(HarvestMoon.root, name.replaceAll("(.)([A-Z])", "$1-$2").toLowerCase() + ".cfg"));
        try {
            config.load();
            ConfigHelper.setConfig(config);
            Class clazz = Class.forName(HMModInfo.JAVAPATH + "config." + name);
            Method method = clazz.getMethod("init", Configuration.class);
            method.invoke(null, config);
        } catch (Exception e) {
            HarvestMoon.logger.log(Level.ERROR, HMModInfo.MODNAME + " failed to load in it's " + name + " config");
            e.printStackTrace();
        } finally {
            config.save();
        }
    }
}
