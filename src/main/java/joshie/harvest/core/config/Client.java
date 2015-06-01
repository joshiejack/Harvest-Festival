package joshie.harvest.core.config;

import static joshie.harvest.core.helpers.generic.ConfigHelper.getBoolean;
import static joshie.harvest.core.helpers.generic.ConfigHelper.setCategory;
import net.minecraftforge.common.config.Configuration;

public class Client {
    public static boolean USE_MODERN_MIXER_RENDER;
    public static boolean USE_MODERN_OVEN_RENDER;
    public static boolean CHICKEN_OFFSET_FIX;

    public static void init(Configuration config) {
        setCategory("Alternative Renders");
        USE_MODERN_MIXER_RENDER = getBoolean("Use Modern Mixer Render", false);
        USE_MODERN_OVEN_RENDER = getBoolean("Use Modern Oven Render", false);
        CHICKEN_OFFSET_FIX = getBoolean("Chicken Mount Player Render Fix", true);
    }
}
