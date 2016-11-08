package joshie.harvest.npc.gift;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.npc.gift.IGiftRegistry;
import joshie.harvest.cooking.HFCooking;
import joshie.harvest.cooking.item.ItemMeal.Meal;
import joshie.harvest.core.helpers.InventoryHelper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import static joshie.harvest.api.npc.gift.GiftCategory.*;
import static joshie.harvest.api.npc.gift.IGiftHandler.Quality.AWESOME;

public class GiftsYulif extends Gifts {
    private boolean isCake(Meal meal) {
        return meal == Meal.CAKE || meal == Meal.CAKE_CHOCOLATE;
    }

    @Override
    public Quality getQuality(ItemStack stack) {
        IGiftRegistry registry = HFApi.npc.getGifts();
        if (InventoryHelper.isOreName(stack, "cropMelon")) return AWESOME;
        else if (stack.getItem() == Items.SUGAR || InventoryHelper.isOreName(stack, "gemQuartz") || stack.getItem() == Item.getItemFromBlock(Blocks.CAKE) ||
                (stack.getItem() == HFCooking.MEAL && isCake(HFCooking.MEAL.getEnumFromStack(stack))) || InventoryHelper.isOreName(stack, "cropCorn") ||
                InventoryHelper.isOreName(stack, "cropPineapple") || registry.isGiftType(stack, BUILDING)) return Quality.GOOD;
        else if (InventoryHelper.isOreName(stack, "cropCarrot") || InventoryHelper.isOreName(stack, "cropWheat")) return Quality.TERRIBLE;
        else if (registry.isGiftType(stack, VEGETABLE, PLANT)) return Quality.BAD;
        else if (registry.isGiftType(stack, MONSTER, ANIMAL, FLOWER)) return Quality.DISLIKE;
        else return super.getQuality(stack);
    }
}