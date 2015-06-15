package joshie.harvest.core.config;

import static joshie.harvest.core.helpers.generic.ConfigHelper.getBoolean;
import net.minecraftforge.common.config.Configuration;

public class Crops {
    public static boolean SEASONAL_BONEMEAL;
    public static boolean ENABLE_BONEMEAL;
    public static boolean ALWAYS_GROW;

    public static void init(Configuration config) {
        ALWAYS_GROW = getBoolean("Crops > Always Grow", false, "This setting when set to true, will make crops grow based on random tick instead of day by day, Take note that this also affects the number of seeds a crop bag will plant. It will only plant 3 seeds instead of a 3x3");
        ENABLE_BONEMEAL = getBoolean("Crops > Enable Bonemeal", false, "Enabling this will allow you to use bonemeal on plants to grow them.");
        SEASONAL_BONEMEAL = getBoolean("Crops > Seasonal Bonemeal", true, "If you have bonemeal enabled, with this setting active, bonemeal will only work when the crop is in season");
    }
}
