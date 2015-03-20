package joshie.harvestmoon.api;

import joshie.harvestmoon.api.commands.IHMCommandHandler;
import joshie.harvestmoon.api.cooking.IFoodRegistry;
import joshie.harvestmoon.api.crops.ICropHandler;

/** These are filled by HarvestMoon when it is loaded **/
public class HMApi {
    public static ICropHandler CROPS = null;
    public static IFoodRegistry COOKING = null;
    public static IHMCommandHandler COMMANDS = null;
}
