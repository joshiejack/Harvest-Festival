package joshie.harvestmoon.config;

import joshie.harvestmoon.HarvestMoon;
import joshie.harvestmoon.lib.HMModInfo;
import net.minecraftforge.common.config.Configuration;

import org.apache.logging.log4j.Level;

public class Tools {
    public static boolean USE_VANILLA_MATERIALS;

    public static void init(Configuration config) {
        try {
            config.load();
            USE_VANILLA_MATERIALS = config.get("Options", "Use Vanilla Materials for Tools", false).getBoolean(false);
        } catch (Exception e) {
            HarvestMoon.logger.log(Level.ERROR, HMModInfo.MODNAME + " failed to load in it's tools config");
            e.printStackTrace();
        } finally {
            config.save();
        }
    }
}
