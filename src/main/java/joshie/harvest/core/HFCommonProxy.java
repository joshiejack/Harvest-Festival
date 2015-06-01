package joshie.harvest.core;

import joshie.harvest.core.handlers.api.HFApiHandler;
import joshie.harvest.core.util.WorldDestroyer;
import joshie.harvest.init.HFAnimals;
import joshie.harvest.init.HFBlocks;
import joshie.harvest.init.HFBuildings;
import joshie.harvest.init.HFCommands;
import joshie.harvest.init.HFConfig;
import joshie.harvest.init.HFCooking;
import joshie.harvest.init.HFCrops;
import joshie.harvest.init.HFEntities;
import joshie.harvest.init.HFGifts;
import joshie.harvest.init.HFHandlers;
import joshie.harvest.init.HFItems;
import joshie.harvest.init.HFMining;
import joshie.harvest.init.HFNPCs;
import joshie.harvest.init.HFPackets;
import joshie.harvest.init.HFQuests;
import joshie.harvest.init.HFShops;
import joshie.harvest.init.HFVanilla;
import joshie.harvest.plugins.HFPlugins;

public class HFCommonProxy {
    public void preInit() {
        HFApiHandler.init();
        HFVanilla.init();
        HFConfig.init();
        HFPlugins.preInit();
        HFCrops.init();
        HFNPCs.preInit();
        HFBlocks.init();
        HFBuildings.preInit();
        HFItems.init();
        HFCooking.init();
        HFEntities.init();
        HFQuests.init();
        HFPackets.init();
        HFHandlers.init();
        HFShops.init();
        HFMining.init();
        HFGifts.init();
        HFAnimals.preInit();
        HFTab.init();
        HFCommands.preInit();
    }

    public void init() {
        HFPlugins.init();
        HFAnimals.init();
        HFBuildings.init();
        HFNPCs.init();
    }

    public void postInit() {
        HFPlugins.postInit();
        WorldDestroyer.replaceWorldProvider();
    }
}
