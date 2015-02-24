package joshie.harvestmoon.core;

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
import joshie.harvestmoon.init.HMOverride;
import joshie.harvestmoon.init.HMPackets;
import joshie.harvestmoon.init.HMQuests;
import joshie.harvestmoon.init.HMShops;

public class HMCommonProxy {
    public void preInit() {
        HMOverride.init();
        HMConfiguration.init();
        HMCrops.init();
        HMNPCs.init();
        HMBlocks.init();
        HMBuildings.init();
        HMItems.init();
        HMCooking.init();
        HMEntities.init();
        HMQuests.init();
        HMPackets.init();
        HMHandlers.init();
        HMShops.init();
        HMMining.init();
        HMGifts.init();
        HMAnimals.init();
        HMTab.init();
    }
    
    public void init() {}
}
