package joshie.harvestmoon.core;

import joshie.harvestmoon.core.handlers.api.HMApiHandler;
import joshie.harvestmoon.core.util.WorldDestroyer;
import joshie.harvestmoon.init.HMAnimals;
import joshie.harvestmoon.init.HMBlocks;
import joshie.harvestmoon.init.HMBuildings;
import joshie.harvestmoon.init.HMConfiguration;
import joshie.harvestmoon.init.HMCooking;
import joshie.harvestmoon.init.HMCrops;
import joshie.harvestmoon.init.HMEntities;
import joshie.harvestmoon.init.HMGifts;
import joshie.harvestmoon.init.HMHandlers;
import joshie.harvestmoon.init.HMItems;
import joshie.harvestmoon.init.HMMining;
import joshie.harvestmoon.init.HMNPCs;
import joshie.harvestmoon.init.HMPackets;
import joshie.harvestmoon.init.HMQuests;
import joshie.harvestmoon.init.HMShops;
import joshie.harvestmoon.init.HMVanilla;
import joshie.harvestmoon.plugins.HMPlugins;

public class HMCommonProxy {
    public void preInit() {
        HMApiHandler.init();
        HMVanilla.init();
        HMConfiguration.init();
        HMPlugins.preInit();
        HMCrops.init();
        HMNPCs.preInit();
        HMBlocks.init();
        HMBuildings.preInit();
        HMItems.init();
        HMCooking.init();
        HMEntities.init();
        HMQuests.init();
        HMPackets.init();
        HMHandlers.init();
        HMShops.init();
        HMMining.init();
        HMGifts.init();
        HMAnimals.preInit();
        HMTab.init();
    }

    public void init() {
        HMPlugins.init();
        HMAnimals.init();
        HMBuildings.init();
        HMNPCs.init();
    }

    public void postInit() {
        HMPlugins.postInit();
        WorldDestroyer.replaceWorldProvider();
    }
}
