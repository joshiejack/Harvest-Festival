package joshie.harvestmoon.core.config;
import static joshie.harvestmoon.core.helpers.generic.ConfigHelper.getBoolean;
import net.minecraftforge.common.config.Configuration;

public class General {
    public static boolean DEBUG_MODE;
    
    public static void init (Configuration config) {
        DEBUG_MODE = getBoolean("Enable Debug Mode", false);
    }
}
