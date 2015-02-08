package joshie.harvestmoon.config;

import joshie.harvestmoon.HarvestMoon;
import joshie.harvestmoon.lib.HMModInfo;
import net.minecraftforge.common.config.Configuration;

import org.apache.logging.log4j.Level;

public class Client {
    public static boolean USE_MODERN_MIXER_RENDER;
    public static boolean USE_MODERN_OVEN_RENDER;

    public static void init(Configuration config) {
        try {
            config.load();
            USE_MODERN_MIXER_RENDER = config.get("Alternative Renders", "Use Modern Mixer Render", false).getBoolean(false);
            USE_MODERN_OVEN_RENDER = config.get("Alternative Renders", "Use Modern Oven Render", false).getBoolean(false);
        } catch (Exception e) {
            HarvestMoon.logger.log(Level.ERROR, HMModInfo.MODNAME + " failed to load in it's client config");
            e.printStackTrace();
        } finally {
            config.save();
        }
    }
}
