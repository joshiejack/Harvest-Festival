package joshie.harvest.core.config;

import net.minecraftforge.common.config.Configuration;

import static joshie.harvest.core.helpers.generic.ConfigHelper.getDouble;

public class General {
    public static double SELL_QUALITY_MODIFIER;
    public static boolean DEBUG_MODE = true;
    
    public static void init (Configuration config) {
        //DEBUG_MODE = getBoolean("Enable Debug Mode", false);
        SELL_QUALITY_MODIFIER = getDouble("Sell Quality Modifier", 0.01825D);
    }
}
