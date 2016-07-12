package joshie.harvest.core;

import joshie.harvest.animals.AnimalRegistry;
import joshie.harvest.api.HFApi;
import joshie.harvest.buildings.BuildingRegistry;
import joshie.harvest.calendar.CalendarAPI;
import joshie.harvest.cooking.FoodRegistry;
import joshie.harvest.core.handlers.ShippingRegistry;
import joshie.harvest.core.handlers.SizeableRegistry;
import joshie.harvest.crops.CropRegistry;
import joshie.harvest.gathering.GatheringRegistry;
import joshie.harvest.npc.NPCRegistry;
import joshie.harvest.player.PlayerAPI;
import joshie.harvest.shops.ShopRegistry;

public class HFApiLoader {
    public static void init() {
        //Register API Handlers
        HFApi.animals = new AnimalRegistry();
        HFApi.buildings = new BuildingRegistry();
        HFApi.calendar = new CalendarAPI();
        HFApi.crops = new CropRegistry();
        HFApi.cooking = new FoodRegistry();
        HFApi.gathering = new GatheringRegistry();
        HFApi.npc = new NPCRegistry();
        HFApi.player = new PlayerAPI();
        HFApi.shops = new ShopRegistry();
        HFApi.shipping = new ShippingRegistry();
        HFApi.sizeable = new SizeableRegistry();
        HFApi.tickable = new HFDailyTickable();
    }
}
