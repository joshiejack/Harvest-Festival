package joshie.harvest.npcs.gift;

import joshie.harvest.api.core.Ore;
import joshie.harvest.cooking.item.ItemMeal.Meal;
import net.minecraft.init.Items;

import static joshie.harvest.api.npc.gift.GiftCategory.*;
import static joshie.harvest.cooking.HFCooking.MEAL;

@SuppressWarnings("unused")
public class GiftsAshlee extends Gifts {
    public GiftsAshlee() {
        stackRegistry.register(Ore.of("cropBanana"), Quality.AWESOME);
        stackRegistry.register(Ore.of("cropCorn"), Quality.AWESOME);
        categoryRegistry.put(EGG, Quality.GOOD);
        categoryRegistry.put(FRUIT, Quality.GOOD);
        stackRegistry.register(MEAL.getStackFromEnum(Meal.KETCHUP), Quality.BAD);
        stackRegistry.register(MEAL.getStackFromEnum(Meal.FRIES_FRENCH), Quality.BAD);
        stackRegistry.register(MEAL.getStackFromEnum(Meal.DOUGHNUT), Quality.BAD);
        stackRegistry.register(MEAL.getStackFromEnum(Meal.POPCORN), Quality.BAD);
        stackRegistry.register(MEAL.getStackFromEnum(Meal.ICE_CREAM), Quality.BAD);
        stackRegistry.register(Items.RABBIT_FOOT, Quality.BAD);
        categoryRegistry.put(MEAT, Quality.BAD);
        stackRegistry.register(Items.BEEF, Quality.TERRIBLE);
        stackRegistry.register(Items.PORKCHOP, Quality.TERRIBLE);
        stackRegistry.register(Items.RABBIT, Quality.TERRIBLE);
        stackRegistry.register(Items.MUTTON, Quality.TERRIBLE);
    }
}
