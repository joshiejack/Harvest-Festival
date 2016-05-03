package joshie.harvest.core;

import joshie.harvest.animals.HFAnimals;
import joshie.harvest.blocks.HFBlocks;
import joshie.harvest.buildings.HFBuildings;
import joshie.harvest.cooking.HFCooking;
import joshie.harvest.cooking.HFIngredients;
import joshie.harvest.cooking.HFRecipes;
import joshie.harvest.core.config.HFConfig;
import joshie.harvest.core.config.HFVanilla;
import joshie.harvest.crops.HFCrops;
import joshie.harvest.items.HFItems;
import joshie.harvest.mining.HFMining;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.npc.gift.init.HFGifts;
import joshie.harvest.plugins.HFPlugins;
import joshie.harvest.quests.HFQuests;
import joshie.harvest.shops.HFShops;

import java.util.ArrayList;
import java.util.List;

public class HFCommonProxy {
    protected static final List<Class> list = new ArrayList<Class>();

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
        list.add(HFIngredients.class);
        list.add(HFRecipes.class);
        list.add(HFQuests.class);
        list.add(HFShops.class);
        list.add(HFMining.class);
        list.add(HFGifts.class);
        list.add(HFAnimals.class);
        list.add(HFTab.class);
        list.add(HFRecipeFixes.class);
    }

    public void load(String stage) {
        //Check stage is client
        if (stage.contains("Client")) {
            if (!isClient()) return;
        }

        //Continue
        for (Class c : list) {
            try {
                c.getMethod(stage).invoke(null);
            } catch (Exception e) { }
        }
    }

    public boolean isClient() {
        return false;
    }
}
