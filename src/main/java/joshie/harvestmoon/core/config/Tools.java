package joshie.harvestmoon.core.config;

import static joshie.harvestmoon.core.helpers.generic.ConfigHelper.getBoolean;
import net.minecraftforge.common.config.Configuration;

public class Tools {
    public static boolean USE_VANILLA_MATERIALS;

    public static void init(Configuration config) {
        USE_VANILLA_MATERIALS = getBoolean("Use Vanilla Materials for Tools", false);
    }
}
