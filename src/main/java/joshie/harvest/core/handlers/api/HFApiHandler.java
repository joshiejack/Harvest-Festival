package joshie.harvest.core.handlers.api;

import joshie.harvest.animals.AnimalRegistry;
import joshie.harvest.api.HFApi;
import joshie.harvest.cooking.FoodRegistry;
import joshie.harvest.init.HFNPCs;
import joshie.harvest.init.HFQuests;
import joshie.harvest.shops.ShopRegistry;

public class HFApiHandler {
    public static void init() {
        HFApi.ANIMALS = new AnimalRegistry();
        HFApi.CROPS = new CropRegistry();
        HFApi.COOKING = new FoodRegistry();
        HFApi.NPC = new HFNPCs();
        HFApi.SHOPS = new ShopRegistry();
        HFApi.QUESTS = new HFQuests();
    }
}
