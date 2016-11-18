package joshie.harvest.npc.gift;

import joshie.harvest.api.core.Ore;
import joshie.harvest.cooking.HFCooking;
import joshie.harvest.cooking.item.ItemIngredients.Ingredient;
import joshie.harvest.cooking.item.ItemMeal.Meal;
import joshie.harvest.mining.HFMining;
import joshie.harvest.mining.item.ItemMaterial.Material;
import net.minecraft.init.Items;

import static joshie.harvest.api.npc.gift.GiftCategory.*;

@SuppressWarnings("unused")
public class GiftsLiara extends Gifts {
    public GiftsLiara() {
        stackRegistry.register(HFCooking.INGREDIENTS.getStackFromEnum(Ingredient.CHOCOLATE), Quality.AWESOME);
        stackRegistry.register(HFCooking.MEAL.getStackFromEnum(Meal.CAKE_CHOCOLATE), Quality.AWESOME);
        stackRegistry.register(HFCooking.MEAL.getStackFromEnum(Meal.CHOCOLATE_HOT), Quality.AWESOME);
        stackRegistry.register(HFCooking.MEAL.getStackFromEnum(Meal.COOKIES_CHOCOLATE), Quality.AWESOME);
        categoryRegistry.put(FRUIT, Quality.GOOD);
        categoryRegistry.put(VEGETABLE, Quality.GOOD);
        categoryRegistry.put(MEAT, Quality.GOOD);
        categoryRegistry.put(ANIMAL, Quality.GOOD);
        stackRegistry.register(Ore.of("dyeBrown"), Quality.GOOD);
        stackRegistry.register(HFMining.MATERIALS.getStackFromEnum(Material.ALEXANDRITE), Quality.DECENT);
        stackRegistry.register(HFMining.MATERIALS.getStackFromEnum(Material.PINK_DIAMOND), Quality.DECENT);
        categoryRegistry.put(FLOWER, Quality.DISLIKE);
        categoryRegistry.put(GEM, Quality.DISLIKE);
        registerWoolLikeItems(Quality.BAD);
        stackRegistry.register(Ore.of("string"), Quality.TERRIBLE);
        stackRegistry.register(Items.SPIDER_EYE, Quality.TERRIBLE);
        stackRegistry.register(Items.FERMENTED_SPIDER_EYE, Quality.TERRIBLE);
    }
}