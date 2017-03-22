package joshie.harvest.npcs.gift;

import joshie.harvest.api.core.Ore;
import joshie.harvest.cooking.HFCooking;
import joshie.harvest.cooking.item.ItemIngredients.Ingredient;
import joshie.harvest.cooking.item.ItemMeal.Meal;
import net.minecraft.init.Items;

import static joshie.harvest.api.npc.gift.GiftCategory.*;
import static joshie.harvest.cooking.HFCooking.MEAL;

@SuppressWarnings("unused")
public class GiftsAbi extends Gifts {
    public GiftsAbi() {
        stackRegistry.register(Items.SUGAR, Quality.AWESOME);
        stackRegistry.register(Items.COOKIE, Quality.AWESOME);
        stackRegistry.register(MEAL.getStackFromEnum(Meal.COOKIES), Quality.AWESOME);
        stackRegistry.register(MEAL.getStackFromEnum(Meal.COOKIES_CHOCOLATE), Quality.AWESOME);
        stackRegistry.register(Items.SADDLE, Quality.GOOD);
        stackRegistry.register(Ore.of("cropApple"), Quality.GOOD);
        stackRegistry.register(Ore.of("cropGrape"), Quality.GOOD);
        stackRegistry.register(Ore.of("cropPotato"), Quality.DECENT);
        stackRegistry.register(Ore.of("cropCarrot"), Quality.DECENT);
        stackRegistry.register(Ore.of("cropCabbage"), Quality.DECENT);
        categoryRegistry.put(MUSHROOM, Quality.DISLIKE);
        categoryRegistry.put(VEGETABLE, Quality.DISLIKE);
        stackRegistry.register(Items.RABBIT_FOOT, Quality.DISLIKE);
        stackRegistry.register(HFCooking.INGREDIENTS.getStackFromEnum(Ingredient.WINE), Quality.DISLIKE);
        stackRegistry.register(Ore.of("leather"), Quality.BAD);
        stackRegistry.register(Items.RABBIT_HIDE, Quality.BAD);
        stackRegistry.register(Items.ROTTEN_FLESH, Quality.TERRIBLE);
        stackRegistry.register(Ore.of("bone"), Quality.TERRIBLE);
        stackRegistry.register(MEAL.getStackFromEnum(Meal.PORRIDGE), Quality.TERRIBLE);
    }
}