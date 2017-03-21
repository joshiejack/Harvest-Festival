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
        stackRegistry.register(Ore.of("cropStrawberry"), Quality.AWESOME);
        stackRegistry.register(Ore.of("cropPineapple"), Quality.GOOD);
        stackRegistry.register(HFCooking.MEAL.getStackFromEnum(Meal.JAM_STRAWBERRY), Quality.DECENT);
        stackRegistry.register(HFCooking.MEAL.getStackFromEnum(Meal.MILK_STRAWBERRY), Quality.DECENT);
        categoryRegistry.put(FLOWER, Quality.DECENT);
        categoryRegistry.put(PLANT, Quality.DECENT);
        categoryRegistry.put(COOKING, Quality.DISLIKE);
        categoryRegistry.put(SWEET, Quality.DISLIKE);
        categoryRegistry.put(KNOWLEDGE, Quality.DISLIKE);
        categoryRegistry.put(GEM, Quality.DISLIKE);
        categoryRegistry.put(MINERAL, Quality.BAD);
        categoryRegistry.put(MEAT, Quality.BAD);
        categoryRegistry.put(FISH, Quality.BAD);
        stackRegistry.register(HFFishing.JUNK.getStackFromEnum(Junk.BONES), Quality.TERRIBLE);
        stackRegistry.register(HFFishing.JUNK.getStackFromEnum(Junk.CAN), Quality.TERRIBLE);
        stackRegistry.register(HFFishing.JUNK.getStackFromEnum(Junk.BOOT), Quality.TERRIBLE);
    }
}