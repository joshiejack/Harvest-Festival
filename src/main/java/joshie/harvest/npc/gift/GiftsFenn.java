package joshie.harvest.npc.gift;

import joshie.harvest.api.core.Ore;
import joshie.harvest.cooking.item.ItemMeal.Meal;

import static joshie.harvest.api.npc.gift.GiftCategory.*;
import static joshie.harvest.cooking.HFCooking.MEAL;

@SuppressWarnings("unused")
public class GiftsFenn extends Gifts {
    public GiftsFenn() {
        stackRegistry.register(MEAL.getStackFromEnum(Meal.SALAD), Quality.AWESOME);
        stackRegistry.register(MEAL.getStackFromEnum(Meal.SPINACH_BOILED), Quality.AWESOME);
        stackRegistry.register(MEAL.getStackFromEnum(Meal.CUCUMBER_PICKLED), Quality.AWESOME);
        stackRegistry.register(MEAL.getStackFromEnum(Meal.JUICE_VEGETABLE), Quality.AWESOME);
        stackRegistry.register(MEAL.getStackFromEnum(Meal.LATTE_VEGETABLE), Quality.AWESOME);
        stackRegistry.register(Ore.of("vine"), Quality.GOOD);
        stackRegistry.register(Ore.of("treeLeaves"), Quality.GOOD);
        categoryRegistry.put(PLANT, Quality.GOOD);
        categoryRegistry.put(GEM, Quality.DISLIKE);
        categoryRegistry.put(MONSTER, Quality.DISLIKE);
        categoryRegistry.put(JUNK, Quality.DISLIKE);
        categoryRegistry.put(SWEET, Quality.BAD);
        stackRegistry.register(Ore.of("cropSweetPotato"), Quality.TERRIBLE);
        stackRegistry.register(Ore.of("cropBeetroot"), Quality.TERRIBLE);
        stackRegistry.register(Ore.of("cropGrape"), Quality.TERRIBLE);
        stackRegistry.register(Ore.of("cropPeach"), Quality.TERRIBLE);
        stackRegistry.register(Ore.of("cropTurnip"), Quality.TERRIBLE);
    }
}
