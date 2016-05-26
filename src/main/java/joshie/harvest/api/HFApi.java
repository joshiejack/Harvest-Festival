package joshie.harvest.api;

import joshie.harvest.api.animals.IAnimalHandler;
import joshie.harvest.api.buildings.IBuildingRegistry;
import joshie.harvest.api.calendar.ICalendar;
import joshie.harvest.api.cooking.IFoodRegistry;
import joshie.harvest.api.core.IDailyTickableRegistry;
import joshie.harvest.api.core.IShippingRegistry;
import joshie.harvest.api.core.ISizeableRegistry;
import joshie.harvest.api.crops.ICropRegistry;
import joshie.harvest.api.gathering.IGatheringRegistry;
import joshie.harvest.api.npc.INPCRegistry;
import joshie.harvest.api.quest.IQuestRegistry;
import joshie.harvest.api.relations.IRelationships;
import joshie.harvest.api.shops.IShopRegistry;

/**
 * These are filled by HarvestFestival when it is loaded
 **/
public class HFApi {
    public static IAnimalHandler animals = null;
    public static IBuildingRegistry buildings = null;
    public static ICalendar calendar = null;
    public static ICropRegistry crops = null;
    public static IDailyTickableRegistry tickable = null;
    public static IFoodRegistry cooking = null;
    public static IGatheringRegistry gathering = null;
    public static INPCRegistry npc = null;
    public static IShopRegistry shops = null;
    public static IQuestRegistry quests = null;
    public static IRelationships relations = null;
    public static IShippingRegistry shipping = null;
    public static ISizeableRegistry sizeable = null;
}