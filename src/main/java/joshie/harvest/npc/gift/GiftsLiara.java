package joshie.harvest.npc.gift;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.npc.gift.GiftCategory;
import joshie.harvest.cooking.HFCooking;
import joshie.harvest.cooking.items.ItemIngredients.Ingredient;
import net.minecraft.item.ItemStack;

public class GiftsLiara extends Gifts {
    @Override
    public Quality getQuality(ItemStack stack) {
        if (stack.getItem() == HFCooking.INGREDIENTS && stack.getItemDamage() == Ingredient.CHOCOLATE.ordinal()) {
            return Quality.AWESOME;
        }

        if (HFApi.npc.getGifts().isGiftType(stack, GiftCategory.COOKING)) {
            return Quality.GOOD;
        }

        if (HFApi.npc.getGifts().isGiftType(stack, GiftCategory.KNITTING)) {
            return Quality.BAD;
        }

        return Quality.DECENT;
    }
}