package joshie.harvestmoon.core.config;

import static joshie.harvestmoon.core.helpers.generic.ConfigHelper.getBoolean;
import net.minecraftforge.common.config.Configuration;

public class Crops {
    public static boolean ALWAYS_GROW;
    
    public static void init(Configuration config) {
        ALWAYS_GROW = getBoolean("Crops > Always Grow", false, "This setting when set to true, will make crops grow based on random tick instead of day by day, Take note that this also affects the number of seeds a crop bag will plant. It will only plant 3 seeds instead of a 3x3");
    }
}
