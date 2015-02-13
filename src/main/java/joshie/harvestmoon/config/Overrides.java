package joshie.harvestmoon.config;

import static joshie.harvestmoon.helpers.generic.ConfigHelper.getBoolean;
import net.minecraftforge.common.config.Configuration;

public class Overrides {
    public static boolean EGG;

    public static void init(Configuration config) {
        EGG = getBoolean("Override Vanilla Eggs", true);
    }
}
