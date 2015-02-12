package joshie.harvestmoon.config;

import static joshie.harvestmoon.helpers.generic.ConfigHelper.getBoolean;
import net.minecraftforge.common.config.Configuration;

public class Animals {
    public static boolean CAN_SPAWN;

    public static void init(Configuration config) {
        CAN_SPAWN = getBoolean("Enable Animal Spawning", false);
    }
}
