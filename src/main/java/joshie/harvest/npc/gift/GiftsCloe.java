package joshie.harvest.npc.gift;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.npc.gift.IGiftRegistry;
import joshie.harvest.core.helpers.InventoryHelper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import static joshie.harvest.api.npc.gift.GiftCategory.*;

public class GiftsCloe extends Gifts {
    @Override
    public Quality getQuality(ItemStack stack) {
        IGiftRegistry registry = HFApi.npc.getGifts();
        if (InventoryHelper.isOreName(stack, "bone") || stack.getItem() == Items.SKULL || stack.getItem() == Items.BOOK
                || stack.getItem() == Items.ENCHANTED_BOOK || stack.getItem() == Items.WRITABLE_BOOK || stack.getItem() == Items.WRITTEN_BOOK) {
            return Quality.AWESOME;
        } else if (registry.isGiftType(stack, MONSTER, KNOWLEDGE)) return Quality.GOOD;
        else if (InventoryHelper.isOreName(stack, "cropCabbage", "cropGreenPepper", "cropSpinach")) return Quality.TERRIBLE;
        else if (stack.getItem() == Items.PUMPKIN_PIE || InventoryHelper.isOreName(stack, "cropCarrot", "cropPumpkin", "cropCorn",
                            "cropOrange", "cropPineapple", "cropStrawberry", "cropSweetPotato", "cropTomato")) return Quality.BAD;
        else if (registry.isGiftType(stack, VEGETABLE, FLOWER)) return Quality.DISLIKE;
        return super.getQuality(stack);
    }
}