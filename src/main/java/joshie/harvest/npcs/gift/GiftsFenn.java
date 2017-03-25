package joshie.harvest.npcs.gift;

import joshie.harvest.api.core.Ore;
import joshie.harvest.cooking.HFCooking;
import joshie.harvest.cooking.item.ItemIngredients.Ingredient;
import joshie.harvest.cooking.item.ItemMeal.Meal;
import net.minecraft.init.Blocks;

import static joshie.harvest.api.npc.gift.GiftCategory.*;
import static joshie.harvest.cooking.HFCooking.MEAL;

@SuppressWarnings("unused")
public class GiftsFenn extends Gifts {
    public GiftsFenn() {
        stackRegistry.register(MEAL.getStackFromEnum(Meal.SALAD), Quality.AWESOME);
        stackRegistry.register(MEAL.getStackFromEnum(Meal.SPINACH_BOILED), Quality.AWESOME);
        stackRegistry.register(Ore.of("blockCactus"), Quality.AWESOME);
        stackRegistry.register(Ore.of("cropSpinach"), Quality.GOOD);
        stackRegistry.register(Ore.of("cropCucumber"), Quality.GOOD);
        stackRegistry.register(Ore.of("vine"), Quality.GOOD);
        stackRegistry.register(Blocks.WATERLILY, Quality.GOOD);
        categoryRegistry.put(MUSHROOM, Quality.GOOD);
        categoryRegistry.put(HERB, Quality.GOOD);
        categoryRegistry.put(PLANT, Quality.DECENT);
        categoryRegistry.put(GEM, Quality.DISLIKE);
        categoryRegistry.put(MONSTER, Quality.DISLIKE);
        stackRegistry.register(HFCooking.INGREDIENTS.getStackFromEnum(Ingredient.CHOCOLATE), Quality.BAD);
        stackRegistry.register(MEAL.getStackFromEnum(Meal.BREAD_RAISIN), Quality.BAD);
        stackRegistry.register(MEAL.getStackFromEnum(Meal.JUICE_GRAPE), Quality.TERRIBLE);
        stackRegistry.register(MEAL.getStackFromEnum(Meal.JAM_GRAPE), Quality.TERRIBLE);
        stackRegistry.register(Ore.of("cropGrape"), Quality.TERRIBLE);
        stackRegistry.register(HFCooking.INGREDIENTS.getStackFromEnum(Ingredient.WINE), Quality.TERRIBLE);
    }
}
