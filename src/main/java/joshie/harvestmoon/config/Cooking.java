package joshie.harvestmoon.config;

import static joshie.harvestmoon.helpers.generic.ConfigHelper.getBoolean;
import net.minecraftforge.common.config.Configuration;

public class Cooking {
    public static boolean ENABLE_STEAMER;

    public static void init(Configuration config) {
        ENABLE_STEAMER = getBoolean("Enable Steamer and Steamer Food", false);
    }
}
