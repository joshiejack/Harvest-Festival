package joshie.harvestmoon.api;

import joshie.harvestmoon.api.registry.ICookingRegistry;
import joshie.harvestmoon.api.registry.ICropRegistry;

/** These are filled by HarvestMoon when it is loaded **/
public class HMApi {
    public static ICropRegistry CROPS = null;
    public static ICookingRegistry COOKING = null;
}
