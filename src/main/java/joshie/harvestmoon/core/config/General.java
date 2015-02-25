package joshie.harvestmoon.core.config;
import static joshie.harvestmoon.core.helpers.generic.ConfigHelper.getDouble;
import net.minecraftforge.common.config.Configuration;

public class General {
    public static double SELL_QUALITY_MODIFIER;
    public static boolean DEBUG_MODE = false;
    
    public static void init (Configuration config) {
        //DEBUG_MODE = getBoolean("Enable Debug Mode", false);
        SELL_QUALITY_MODIFIER = getDouble("Sell Quality Modifier", 0.01825D);
    }
}
