package joshie.harvest.npcs.gift;

import joshie.harvest.api.core.Ore;
import joshie.harvest.cooking.HFCooking;
import joshie.harvest.cooking.item.ItemIngredients.Ingredient;
import joshie.harvest.cooking.item.ItemMeal.Meal;
import net.minecraft.init.Items;

import static joshie.harvest.api.npc.gift.GiftCategory.*;

@SuppressWarnings("unused")
public class GiftsLiara extends Gifts {
    public GiftsLiara() {
        stackRegistry.register(HFCooking.INGREDIENTS.getStackFromEnum(Ingredient.CHOCOLATE), Quality.AWESOME);
        stackRegistry.register(HFCooking.MEAL.getStackFromEnum(Meal.CAKE_CHOCOLATE), Quality.AWESOME);
        stackRegistry.register(HFCooking.MEAL.getStackFromEnum(Meal.CHOCOLATE_HOT), Quality.AWESOME);
        stackRegistry.register(HFCooking.MEAL.getStackFromEnum(Meal.COOKIES_CHOCOLATE), Quality.AWESOME);
        categoryRegistry.put(HERB, Quality.GOOD);
        categoryRegistry.put(FRUIT, Quality.GOOD);
        categoryRegistry.put(VEGETABLE, Quality.GOOD);
        categoryRegistry.put(MUSHROOM, Quality.GOOD);
        categoryRegistry.put(MEAT, Quality.GOOD);
        categoryRegistry.put(EGG, Quality.GOOD);
        categoryRegistry.put(MILK, Quality.GOOD);
        categoryRegistry.put(MONEY, Quality.DECENT);
        stackRegistry.register(Ore.of("dyeBrown"), Quality.GOOD);
        categoryRegistry.put(FLOWER, Quality.DECENT);
        categoryRegistry.put(GEM, Quality.DISLIKE);
        categoryRegistry.put(WOOL, Quality.BAD);
        stackRegistry.register(Ore.of("string"), Quality.TERRIBLE);
        stackRegistry.register(Items.SPIDER_EYE, Quality.TERRIBLE);
        stackRegistry.register(Items.FERMENTED_SPIDER_EYE, Quality.TERRIBLE);
    }
}