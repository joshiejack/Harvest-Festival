package joshie.harvest.npcs.gift.init;

import joshie.harvest.api.npc.gift.GiftCategory;
import joshie.harvest.cooking.item.ItemIngredients.Ingredient;
import joshie.harvest.cooking.item.ItemMeal;
import joshie.harvest.cooking.item.ItemMeal.Meal;
import joshie.harvest.core.util.annotations.HFLoader;

import static joshie.harvest.api.npc.gift.GiftCategory.*;
import static joshie.harvest.cooking.HFCooking.INGREDIENTS;
import static joshie.harvest.cooking.HFCooking.MEAL;

@HFLoader(priority = 0)
public class HFGiftsCooking extends HFGiftsAbstract {
    private static final GiftCategory[] DEFAULT = new GiftCategory[] { COOKING };
    private static final GiftCategory[] SWEETS = new GiftCategory[] { COOKING, SWEET };
    private static final GiftCategory[] VEGGIES = new GiftCategory[] { COOKING, VEGETABLE };
    private static final GiftCategory[] FRUITY = new GiftCategory[] { COOKING, FRUIT };
    private static final GiftCategory[] MIXED = new GiftCategory[] { COOKING, FRUIT, VEGETABLE };
    private static GiftCategory[] getCategory(Meal meal) {
        switch (meal) {
            case JUICE_TOMATO:
            case JUICE_VEGETABLE:
            case LATTE_VEGETABLE:
            case TURNIP_PICKLED:
            case SALAD:
            case SPINACH_BOILED:
            case CORN_BAKED:
                return VEGGIES;
            case JUICE_MIX:
            case LATTE_MIX:
                return MIXED;
            case SANDWICH_FRUIT:
            case JUICE_PINEAPPLE:
            case JUICE_GRAPE:
            case JUICE_PEACH:
            case JUICE_BANANA:
            case JUICE_ORANGE:
            case JUICE_APPLE:
            case JUICE_FRUIT:
            case LATTE_FRUIT:
            case CUCUMBER_PICKLED:
                return FRUITY;
            case COOKIES:
            case COOKIES_CHOCOLATE:
            case CAKE_CHOCOLATE:
            case ICE_CREAM:
            case BUN_JAM:
            case CAKE:
            case CHOCOLATE_HOT:
            case DOUGHNUT:
            case PANCAKE:
            case POPCORN:
            case FRIES_FRENCH:
                return SWEETS;
            default: return DEFAULT;
        }
    }

    public static void init() {
        for (Meal meal: ItemMeal.Meal.values()) {
            assignGeneric(MEAL.getStackFromEnum(meal), getCategory(meal));
        }

        assignGeneric(INGREDIENTS.getStackFromEnum(Ingredient.CHOCOLATE), SWEET);
        assignGeneric(INGREDIENTS.getStackFromEnum(Ingredient.DUMPLING_POWDER), JUNK);
        assignGeneric(INGREDIENTS.getStackFromEnum(Ingredient.WINE), COOKING);
        assignGeneric(INGREDIENTS.getStackFromEnum(Ingredient.FLOUR), JUNK);
        assignGeneric(INGREDIENTS.getStackFromEnum(Ingredient.OIL), JUNK);
        assignGeneric(INGREDIENTS.getStackFromEnum(Ingredient.RICEBALL), JUNK);
        assignGeneric(INGREDIENTS.getStackFromEnum(Ingredient.SALT), JUNK);
    }
}