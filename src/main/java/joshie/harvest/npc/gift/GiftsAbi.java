package joshie.harvest.npc.gift;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.npc.gift.GiftCategory;
import joshie.harvest.api.npc.gift.IGiftRegistry;
import joshie.harvest.cooking.HFCooking;
import joshie.harvest.cooking.item.ItemMeal.Meal;
import joshie.harvest.core.helpers.InventoryHelper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class GiftsAbi extends Gifts {
    private boolean isCookies(Meal meal) {
        return meal == Meal.COOKIES || meal == Meal.COOKIES_CHOCOLATE;
    }

    @Override
    public Quality getQuality(ItemStack stack) {
        IGiftRegistry registry = HFApi.npc.getGifts();
        if (stack.getItem() == Items.SUGAR || stack.getItem() == Items.COOKIE ||
                (stack.getItem() == HFCooking.MEAL && isCookies(HFCooking.MEAL.getEnumFromStack(stack)))) return Quality.AWESOME;
        else if (registry.isGiftType(stack, GiftCategory.SWEET)) return Quality.GOOD;
        else if (stack.getItem() == Items.ROTTEN_FLESH || stack.getItem() == Items.SKULL || InventoryHelper.isOreName(stack, "bone")) return Quality.TERRIBLE;
        else if (stack.getItem() == Items.MAGMA_CREAM || InventoryHelper.isOreName(stack, "leather") || InventoryHelper.isOreName(stack, "slimeball")) return Quality.BAD;
        else if (registry.isGiftType(stack, GiftCategory.MONSTER)) return Quality.DISLIKE;
        else return super.getQuality(stack);
    }
}