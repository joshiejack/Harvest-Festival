package joshie.harvest.npc.gift.init;

import joshie.harvest.cooking.item.ItemIngredients.Ingredient;
import joshie.harvest.core.util.annotations.HFLoader;

import static joshie.harvest.api.npc.gift.GiftCategory.COOKING;
import static joshie.harvest.api.npc.gift.GiftCategory.JUNK;
import static joshie.harvest.api.npc.gift.GiftCategory.SWEET;
import static joshie.harvest.cooking.HFCooking.INGREDIENTS;
import static joshie.harvest.cooking.HFCooking.MEAL;

@HFLoader(priority = 0)
public class HFGiftsCooking extends HFGiftsAbstract {
    public static void init() {
        assignGeneric(MEAL, COOKING);
        assignGeneric(INGREDIENTS.getStackFromEnum(Ingredient.CHOCOLATE), SWEET);
        assignGeneric(INGREDIENTS.getStackFromEnum(Ingredient.DUMPLING_POWDER), JUNK);
        assignGeneric(INGREDIENTS.getStackFromEnum(Ingredient.WINE), COOKING);
        assignGeneric(INGREDIENTS.getStackFromEnum(Ingredient.FLOUR), JUNK);
        assignGeneric(INGREDIENTS.getStackFromEnum(Ingredient.OIL), JUNK);
        assignGeneric(INGREDIENTS.getStackFromEnum(Ingredient.RICEBALL), JUNK);
        assignGeneric(INGREDIENTS.getStackFromEnum(Ingredient.SALT), JUNK);
    }
}