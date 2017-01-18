package joshie.harvest.npcs.gift;

import joshie.harvest.api.core.Ore;
import joshie.harvest.cooking.item.ItemMeal.Meal;
import net.minecraft.init.Items;

import static joshie.harvest.api.npc.gift.GiftCategory.*;
import static joshie.harvest.cooking.HFCooking.MEAL;

@SuppressWarnings("unused")
public class GiftsGirafi extends Gifts {
    public GiftsGirafi() {
        stackRegistry.register(Ore.of("cropPotato"), Quality.AWESOME);
        stackRegistry.register(Items.BAKED_POTATO, Quality.AWESOME);
        stackRegistry.register(MEAL.getStackFromEnum(Meal.FRIES_FRENCH), Quality.AWESOME);
        categoryRegistry.put(ANIMAL, Quality.GOOD);
        categoryRegistry.put(FLOWER, Quality.DISLIKE);
        categoryRegistry.put(SWEET, Quality.BAD);
        registerWoolLikeItems(Quality.TERRIBLE);
    }
}