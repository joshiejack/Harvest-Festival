package joshie.harvest.npc.gift;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.npc.gift.IGiftRegistry;
import joshie.harvest.core.helpers.InventoryHelper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import static joshie.harvest.api.npc.gift.GiftCategory.*;
import static joshie.harvest.npc.gift.GiftsKatlin.isWoolLikeItem;

public class GiftsGirafi extends Gifts {
    @Override
    public Quality getQuality(ItemStack stack) {
        IGiftRegistry registry = HFApi.npc.getGifts();
        if (InventoryHelper.isOreName(stack, "cropPotato") || stack.getItem() == Items.BAKED_POTATO) return Quality.AWESOME;
        else if (registry.isGiftType(stack, ANIMAL)) return Quality.GOOD;
        else if (isWoolLikeItem(stack)) return Quality.TERRIBLE;
        else if (registry.isGiftType(stack, SWEET)) return Quality.BAD;
        else if (registry.isGiftType(stack, FLOWER)) return Quality.DISLIKE;
        else return super.getQuality(stack);
    }
}