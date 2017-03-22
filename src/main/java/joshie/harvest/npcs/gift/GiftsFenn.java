package joshie.harvest.npcs.gift;

import joshie.harvest.api.core.Ore;
import joshie.harvest.cooking.HFCooking;
import joshie.harvest.cooking.item.ItemIngredients.Ingredient;
import joshie.harvest.cooking.item.ItemMeal.Meal;
import net.minecraft.init.Items;

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
        stackRegistry.register(Items.RABBIT_FOOT, Quality.GOOD);
        categoryRegistry.put(PLANT, Quality.GOOD);
        categoryRegistry.put(MUSHROOM, Quality.GOOD);
        categoryRegistry.put(GEM, Quality.DISLIKE);
        categoryRegistry.put(MONSTER, Quality.DISLIKE);
        categoryRegistry.put(JUNK, Quality.DISLIKE);
        stackRegistry.register(HFCooking.INGREDIENTS.getStackFromEnum(Ingredient.WINE), Quality.DISLIKE);
        stackRegistry.register(Items.CAKE, Quality.BAD);
        stackRegistry.register(Items.COOKIE, Quality.BAD);
        stackRegistry.register(MEAL.getStackFromEnum(Meal.CAKE), Quality.BAD);
        stackRegistry.register(MEAL.getStackFromEnum(Meal.CAKE_CHOCOLATE), Quality.BAD);
        stackRegistry.register(MEAL.getStackFromEnum(Meal.COOKIES), Quality.BAD);
        stackRegistry.register(MEAL.getStackFromEnum(Meal.COOKIES_CHOCOLATE), Quality.BAD);
        stackRegistry.register(Ore.of("cropSweetPotato"), Quality.TERRIBLE);
        stackRegistry.register(Ore.of("cropBeetroot"), Quality.TERRIBLE);
        stackRegistry.register(Ore.of("cropGrape"), Quality.TERRIBLE);
        stackRegistry.register(Ore.of("cropPeach"), Quality.TERRIBLE);
        stackRegistry.register(Ore.of("cropTurnip"), Quality.TERRIBLE);
    }
}
