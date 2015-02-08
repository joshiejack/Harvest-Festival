package joshie.harvestmoon.config;

import joshie.harvestmoon.HarvestMoon;
import joshie.harvestmoon.lib.HMModInfo;
import net.minecraftforge.common.config.Configuration;

import org.apache.logging.log4j.Level;

public class Cooking {
    public static boolean ENABLE_STEAMER;
    
    public static void init(Configuration config) {
        try {
            config.load();
            ENABLE_STEAMER = config.get("Extras", "Enable Steamer and Steamer Food", false).getBoolean(false);
        } catch (Exception e) {
            HarvestMoon.logger.log(Level.ERROR, HMModInfo.MODNAME + " failed to load in it's cooking config");
            e.printStackTrace();
        } finally {
            config.save();
        }
    }
}
