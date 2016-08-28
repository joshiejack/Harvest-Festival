package joshie.harvest.api;

import joshie.harvest.api.animals.IAnimalHandler;
import joshie.harvest.api.buildings.IBuildingRegistry;
import joshie.harvest.api.calendar.CalendarManager;
import joshie.harvest.api.cooking.CookingManager;
import joshie.harvest.api.core.IDailyTickableRegistry;
import joshie.harvest.api.core.IShippingRegistry;
import joshie.harvest.api.core.ISizeableRegistry;
import joshie.harvest.api.crops.ICropRegistry;
import joshie.harvest.api.gathering.IGatheringRegistry;
import joshie.harvest.api.npc.INPCRegistry;
import joshie.harvest.api.player.IPlayerHelper;
import joshie.harvest.api.shops.IShopRegistry;

/**
 * These are filled by HarvestFestival when it is loaded
 **/
public class HFApi {
    public static IAnimalHandler animals = null;
    public static IBuildingRegistry buildings = null;
    public static CalendarManager calendar = null;
    public static ICropRegistry crops = null;
    public static CookingManager cooking = null;
    public static IGatheringRegistry gathering = null;
    public static INPCRegistry npc = null;
    public static IPlayerHelper player = null;
    public static IShopRegistry shops = null;
    public static IShippingRegistry shipping = null;
    public static ISizeableRegistry sizeable = null;
    public static IDailyTickableRegistry tickable = null;
}