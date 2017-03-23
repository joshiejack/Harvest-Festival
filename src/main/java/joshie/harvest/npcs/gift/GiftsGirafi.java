package joshie.harvest.npcs.gift;

import joshie.harvest.animals.HFAnimals;
import joshie.harvest.animals.item.ItemAnimalProduct.Sizeable;
import joshie.harvest.api.core.Ore;
import joshie.harvest.api.core.Size;
import joshie.harvest.cooking.item.ItemIngredients.Ingredient;
import joshie.harvest.cooking.item.ItemMeal.Meal;
import net.minecraft.init.Items;

import static joshie.harvest.api.npc.gift.GiftCategory.*;
import static joshie.harvest.cooking.HFCooking.INGREDIENTS;
import static joshie.harvest.cooking.HFCooking.MEAL;

@SuppressWarnings("unused")
public class GiftsGirafi extends Gifts {
    public GiftsGirafi() {
        stackRegistry.register(Ore.of("cropPotato"), Quality.AWESOME);
        stackRegistry.register(Items.BAKED_POTATO, Quality.AWESOME);
        stackRegistry.register(MEAL.getStackFromEnum(Meal.FRIES_FRENCH), Quality.AWESOME);
        stackRegistry.register(Ore.of("cropTomato"), Quality.GOOD);
        categoryRegistry.put(WOOL, Quality.GOOD);
        categoryRegistry.put(FLOWER, Quality.DECENT);
        categoryRegistry.put(EGG, Quality.DISLIKE);
        stackRegistry.register(MEAL.getStackFromEnum(Meal.EGG_BOILED), Quality.DISLIKE);
        stackRegistry.register(MEAL.getStackFromEnum(Meal.EGG_SCRAMBLED), Quality.DISLIKE);
        stackRegistry.register(MEAL.getStackFromEnum(Meal.EGG_OVERRICE), Quality.DISLIKE);
        stackRegistry.register(HFAnimals.ANIMAL_PRODUCT.getStack(Sizeable.MAYONNAISE, Size.SMALL), Quality.DISLIKE);
        stackRegistry.register(HFAnimals.ANIMAL_PRODUCT.getStack(Sizeable.MAYONNAISE, Size.MEDIUM), Quality.DISLIKE);
        stackRegistry.register(HFAnimals.ANIMAL_PRODUCT.getStack(Sizeable.MAYONNAISE, Size.LARGE), Quality.DISLIKE);
        stackRegistry.register(INGREDIENTS.getStackFromEnum(Ingredient.CHOCOLATE), Quality.BAD);
        stackRegistry.register(MEAL.getStackFromEnum(Meal.CAKE_CHOCOLATE), Quality.BAD);
        stackRegistry.register(MEAL.getStackFromEnum(Meal.COOKIES_CHOCOLATE), Quality.BAD);
        stackRegistry.register(MEAL.getStackFromEnum(Meal.CHOCOLATE_HOT), Quality.BAD);
        stackRegistry.register(MEAL.getStackFromEnum(Meal.MILK_HOT), Quality.BAD);
        stackRegistry.register(MEAL.getStackFromEnum(Meal.MILK_STRAWBERRY), Quality.BAD);
        categoryRegistry.put(MILK, Quality.BAD);
        stackRegistry.register(Ore.of("cropPumpkin"), Quality.TERRIBLE);
        stackRegistry.register(Items.PUMPKIN_PIE, Quality.TERRIBLE);
        stackRegistry.register(MEAL.getStackFromEnum(Meal.STEW_PUMPKIN), Quality.TERRIBLE);
    }
}