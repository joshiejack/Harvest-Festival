package joshie.harvest.npc.gift;

import joshie.harvest.cooking.HFCooking;
import joshie.harvest.cooking.items.ItemIngredients.Ingredient;
import net.minecraft.item.ItemStack;

public class GiftsLiara extends Gifts {
    @Override
    public Quality getQuality(ItemStack stack) {
        if (stack.getItem() == HFCooking.INGREDIENTS && stack.getItemDamage() == Ingredient.CHOCOLATE.ordinal()) {
            return Quality.AWESOME;
        }

        if (is(stack, Category.COOKING)) {
            return Quality.GOOD;
        }

        if (is(stack, Category.KNITTING)) {
            return Quality.BAD;
        }

        return Quality.DECENT;
    }
}