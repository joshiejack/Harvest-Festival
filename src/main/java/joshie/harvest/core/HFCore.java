package joshie.harvest.core;

import joshie.harvest.HarvestFestival;
import joshie.harvest.core.handlers.GuiHandler;
import joshie.harvest.core.util.WorldDestroyer;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import static joshie.harvest.core.helpers.generic.ConfigHelper.getInteger;

public class HFCore {
     public static void preInit() {
        WorldDestroyer.replaceWorldProvider();
        NetworkRegistry.INSTANCE.registerGuiHandler(HarvestFestival.instance, new GuiHandler());
    }

    //Configure
    public static boolean DEBUG_MODE = true;
    public static int MINING_ID;
    public static int OVERWORLD_ID;

    public static void configure () {
        OVERWORLD_ID = getInteger("Overworld ID", 3);
        MINING_ID = getInteger("Mining World ID", 4);
    }
}
