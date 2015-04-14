package joshie.harvestmoon.core.handlers.api;

import joshie.harvestmoon.api.HMApi;
import joshie.harvestmoon.cooking.FoodRegistry;
import joshie.harvestmoon.core.commands.CommandManager;
import joshie.harvestmoon.init.HMNPCs;
import joshie.harvestmoon.init.HMQuests;
import joshie.harvestmoon.shops.ShopRegistry;

public class HMApiHandler {
    public static void init() {
        HMApi.CROPS = new CropRegistry();
        HMApi.COOKING = new FoodRegistry();
        HMApi.COMMANDS = CommandManager.INSTANCE;
        HMApi.NPC = new HMNPCs();
        HMApi.SHOPS = new ShopRegistry();
        HMApi.QUESTS = new HMQuests();
    }
}
