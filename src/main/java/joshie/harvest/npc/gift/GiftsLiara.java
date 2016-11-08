package joshie.harvest.npc.gift;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.npc.gift.IGiftRegistry;
import joshie.harvest.cooking.HFCooking;
import joshie.harvest.cooking.item.ItemIngredients.Ingredient;
import joshie.harvest.cooking.item.ItemMeal.Meal;
import joshie.harvest.core.helpers.InventoryHelper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import static joshie.harvest.api.npc.gift.GiftCategory.*;
import static joshie.harvest.npc.gift.GiftsKatlin.isWoolLikeItem;

public class GiftsLiara extends Gifts {
    private boolean isChocolate(Meal meal) {
        return meal == Meal.COOKIES_CHOCOLATE || meal == Meal.CAKE_CHOCOLATE || meal == Meal.CHOCOLATE_HOT;
    }

    @Override
    public Quality getQuality(ItemStack stack) {
        IGiftRegistry registry = HFApi.npc.getGifts();
        if ((stack.getItem() == HFCooking.INGREDIENTS && stack.getItemDamage() == Ingredient.CHOCOLATE.ordinal()) ||
                (stack.getItem() == HFCooking.MEAL && isChocolate(HFCooking.MEAL.getEnumFromStack(stack)))) return Quality.AWESOME;
        else if (InventoryHelper.isOreName(stack, "dyeBrown") || registry.isGiftType(stack, FRUIT, VEGETABLE, MEAT, ANIMAL)) return Quality.GOOD;
        else if (InventoryHelper.isOreName(stack, "string") || stack.getItem() == Items.SPIDER_EYE || stack.getItem() == Items.FERMENTED_SPIDER_EYE) return Quality.TERRIBLE;
        else if (isWoolLikeItem(stack)) return Quality.BAD;
        else if (registry.isGiftType(stack, FLOWER, GEM)) return Quality.DISLIKE;
        else return super.getQuality(stack);
    }
}