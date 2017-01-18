package joshie.harvest.npcs.gift;

import joshie.harvest.api.core.Ore;
import joshie.harvest.cooking.HFCooking;
import joshie.harvest.cooking.item.ItemMeal.Meal;
import joshie.harvest.fishing.HFFishing;
import joshie.harvest.fishing.item.ItemJunk.Junk;

import static joshie.harvest.api.npc.gift.GiftCategory.*;

@SuppressWarnings("unused")
public class GiftsGoddess extends Gifts {
    public GiftsGoddess() {
        stackRegistry.register(Ore.of("cropStrawberry"), Quality.GOOD);
        categoryRegistry.put(FLOWER, Quality.DECENT);
        categoryRegistry.put(COOKING, Quality.DECENT);
        categoryRegistry.put(SWEET, Quality.DISLIKE);
        categoryRegistry.put(KNOWLEDGE, Quality.DISLIKE);
        categoryRegistry.put(MINERAL, Quality.BAD);
        categoryRegistry.put(MEAT, Quality.BAD);
        categoryRegistry.put(GEM, Quality.BAD);
        categoryRegistry.put(PLANT, Quality.BAD);
        stackRegistry.register(HFCooking.MEAL.getStackFromEnum(Meal.JAM_STRAWBERRY), Quality.GOOD);
        stackRegistry.register(HFCooking.MEAL.getStackFromEnum(Meal.MILK_STRAWBERRY), Quality.GOOD);
        stackRegistry.register(HFFishing.JUNK.getStackFromEnum(Junk.BOOT), Quality.TERRIBLE);
    }
}