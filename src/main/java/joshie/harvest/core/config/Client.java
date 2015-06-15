package joshie.harvest.core.config;

import static joshie.harvest.core.helpers.generic.ConfigHelper.getBoolean;
import static joshie.harvest.core.helpers.generic.ConfigHelper.setCategory;
import net.minecraftforge.common.config.Configuration;

public class Client {
    public static boolean CHICKEN_OFFSET_FIX;

    public static void init(Configuration config) {
        setCategory("Alternative Renders");
        CHICKEN_OFFSET_FIX = getBoolean("Chicken Mount Player Render Fix", true);
    }
}
