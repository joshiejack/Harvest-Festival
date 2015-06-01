package joshie.harvest.core.handlers.api;

import joshie.harvest.api.HFApi;
import joshie.harvest.cooking.FoodRegistry;
import joshie.harvest.core.commands.CommandManager;
import joshie.harvest.init.HFNPCs;
import joshie.harvest.init.HFQuests;
import joshie.harvest.shops.ShopRegistry;

public class HFApiHandler {
    public static void init() {
        HFApi.CROPS = new CropRegistry();
        HFApi.COOKING = new FoodRegistry();
        HFApi.COMMANDS = CommandManager.INSTANCE;
        HFApi.NPC = new HFNPCs();
        HFApi.SHOPS = new ShopRegistry();
        HFApi.QUESTS = new HFQuests();
    }
}
