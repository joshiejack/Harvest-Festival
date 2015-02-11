package joshie.harvestmoon.config;

import static joshie.harvestmoon.helpers.generic.ConfigHelper.getBoolean;
import static joshie.harvestmoon.helpers.generic.ConfigHelper.setCategory;
import net.minecraftforge.common.config.Configuration;

public class Client {
    public static boolean USE_MODERN_MIXER_RENDER;
    public static boolean USE_MODERN_OVEN_RENDER;

    public static void init(Configuration config) {
        setCategory("Alternative Renders");
        USE_MODERN_MIXER_RENDER = getBoolean("Use Modern Mixer Render", false);
        USE_MODERN_OVEN_RENDER = getBoolean("Use Modern Oven Render", false);
    }
}
