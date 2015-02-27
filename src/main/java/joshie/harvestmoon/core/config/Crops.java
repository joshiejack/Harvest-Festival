package joshie.harvestmoon.core.config;

import static joshie.harvestmoon.core.helpers.generic.ConfigHelper.getBoolean;
import static joshie.harvestmoon.core.helpers.generic.ConfigHelper.getInteger;
import net.minecraftforge.common.config.Configuration;

public class Crops {
    public static boolean ENABLE_BONEMEAL;
    public static boolean ALWAYS_GROW;
    public static int CROP_QUALITY_LOOP;

    public static void init(Configuration config) {
        ALWAYS_GROW = getBoolean("Crops > Always Grow", false, "This setting when set to true, will make crops grow based on random tick instead of day by day, Take note that this also affects the number of seeds a crop bag will plant. It will only plant 3 seeds instead of a 3x3");
        CROP_QUALITY_LOOP = getInteger("Crops > Loop Value Creative Menu", 100, "If you set this to 1, it will show every single quality of seed or crop. The default of 100, only shows the crops with a 0 rating.");
        ENABLE_BONEMEAL = getBoolean("Crops > Enable Bonemeal", false, "Enabling this will allow you to use bonemeal on plants to grow them.");
    }
}
