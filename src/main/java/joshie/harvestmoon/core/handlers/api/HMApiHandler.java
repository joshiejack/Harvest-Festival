package joshie.harvestmoon.core.handlers.api;

import joshie.harvestmoon.api.HMApi;

public class HMApiHandler {
    public static void init() {
        HMApi.CROPS = new CropRegistry();
        HMApi.COOKING = new CookingRegistry();
    }
}
