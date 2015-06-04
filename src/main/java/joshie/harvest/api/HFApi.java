package joshie.harvest.api;

import joshie.harvest.api.animals.IAnimalHandler;
import joshie.harvest.api.cooking.IFoodRegistry;
import joshie.harvest.api.crops.ICropHandler;
import joshie.harvest.api.npc.INPCRegistry;
import joshie.harvest.api.quest.IQuestRegistry;
import joshie.harvest.api.shops.IShopRegistry;

/** These are filled by HarvestFestival when it is loaded **/
public class HFApi {
    public static IAnimalHandler ANIMALS = null;
    public static ICropHandler CROPS = null;
    public static IFoodRegistry COOKING = null;
    public static INPCRegistry NPC = null;
    public static IShopRegistry SHOPS = null;
    public static IQuestRegistry QUESTS = null;
}
