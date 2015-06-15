package joshie.harvest.core;

import java.util.ArrayList;
import java.util.List;

import joshie.harvest.init.HFAnimals;
import joshie.harvest.init.HFBlocks;
import joshie.harvest.init.HFBuildings;
import joshie.harvest.init.HFCommands;
import joshie.harvest.init.HFConfig;
import joshie.harvest.init.HFCooking;
import joshie.harvest.init.HFCore;
import joshie.harvest.init.HFCrops;
import joshie.harvest.init.HFGifts;
import joshie.harvest.init.HFItems;
import joshie.harvest.init.HFMining;
import joshie.harvest.init.HFNPCs;
import joshie.harvest.init.HFQuests;
import joshie.harvest.init.HFRecipeFixes;
import joshie.harvest.init.HFShops;
import joshie.harvest.init.HFVanilla;
import joshie.harvest.plugins.HFPlugins;

public class HFCommonProxy {
    protected static final List<Class> list = new ArrayList();
    static {
        list.add(HFCore.class);
        list.add(HFVanilla.class);
        list.add(HFConfig.class);
        list.add(HFPlugins.class);
        list.add(HFCrops.class);
        list.add(HFNPCs.class);
        list.add(HFBlocks.class);
        list.add(HFBuildings.class);
        list.add(HFItems.class);
        list.add(HFCooking.class);
        list.add(HFQuests.class);
        list.add(HFShops.class);
        list.add(HFMining.class);
        list.add(HFGifts.class);
        list.add(HFAnimals.class);
        list.add(HFTab.class);
        list.add(HFCommands.class);
        list.add(HFRecipeFixes.class);
    }

    public void load(String stage) {
        //Check stage is client
        if (stage.equals("initClient")) {
            if (!isClient()) return;
        }
        
        //Continue
        for (Class c : list) {
            try {
                c.getMethod(stage).invoke(null);
            } catch (Exception e) {}
        }
    }

    public boolean isClient() {
        return false;
    }
}
