package joshie.harvest.npc.gift;

import joshie.harvest.api.core.Ore;
import joshie.harvest.cooking.HFCooking;
import joshie.harvest.cooking.item.ItemMeal.Meal;
import net.minecraft.init.Items;

import static joshie.harvest.api.npc.gift.GiftCategory.SWEET;
import static joshie.harvest.api.npc.gift.GiftCategory.VEGETABLE;

@SuppressWarnings("unused")
public class GiftsAbi extends Gifts {
    public GiftsAbi() {
        stackRegistry.register(Items.SUGAR, Quality.AWESOME);
        stackRegistry.register(Items.COOKIE, Quality.AWESOME);
        stackRegistry.register(HFCooking.MEAL.getStackFromEnum(Meal.COOKIES), Quality.AWESOME);
        stackRegistry.register(HFCooking.MEAL.getStackFromEnum(Meal.COOKIES_CHOCOLATE), Quality.AWESOME);
        categoryRegistry.put(SWEET, Quality.GOOD);
        categoryRegistry.put(VEGETABLE, Quality.DISLIKE);
        stackRegistry.register(Ore.of("leather"), Quality.BAD);
        stackRegistry.register(Items.MAGMA_CREAM, Quality.TERRIBLE);
        stackRegistry.register(Items.ROTTEN_FLESH, Quality.TERRIBLE);
        stackRegistry.register(Items.SKULL, Quality.TERRIBLE);
        stackRegistry.register(Ore.of("bone"), Quality.TERRIBLE);
        stackRegistry.register(Ore.of("slimeball"), Quality.TERRIBLE);
    }
}