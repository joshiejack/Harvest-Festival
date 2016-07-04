package joshie.harvest.core;

import joshie.harvest.animals.AnimalRegistry;
import joshie.harvest.api.HFApi;
import joshie.harvest.buildings.BuildingRegistry;
import joshie.harvest.calendar.CalendarHelper;
import joshie.harvest.cooking.FoodRegistry;
import joshie.harvest.core.handlers.ShippingRegistry;
import joshie.harvest.core.handlers.SizeableRegistry;
import joshie.harvest.crops.CropRegistry;
import joshie.harvest.npc.NPCRegistry;
import joshie.harvest.npc.gift.GiftRegistry;
import joshie.harvest.npc.town.gathering.GatheringRegistry;
import joshie.harvest.player.relationships.RelationshipHelper;
import joshie.harvest.quests.QuestRegistry;
import joshie.harvest.shops.ShopRegistry;

public class HFApiLoader {
    public static void init() {
        //Register API Handlers
        HFApi.animals = new AnimalRegistry();
        HFApi.buildings = new BuildingRegistry();
        HFApi.calendar = new CalendarHelper();
        HFApi.crops = new CropRegistry();
        HFApi.cooking = new FoodRegistry();
        HFApi.gathering = new GatheringRegistry();
        HFApi.gifts = new GiftRegistry();
        HFApi.npc = new NPCRegistry();
        HFApi.shops = new ShopRegistry();
        HFApi.quests = new QuestRegistry();
        HFApi.relations = new RelationshipHelper();
        HFApi.shipping = new ShippingRegistry();
        HFApi.sizeable = new SizeableRegistry();
        HFApi.tickable = new HFDailyTickable();
    }
}
