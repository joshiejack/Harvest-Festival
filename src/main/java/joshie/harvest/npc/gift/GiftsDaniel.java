package joshie.harvest.npc.gift;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.npc.gift.IGiftRegistry;
import joshie.harvest.core.helpers.InventoryHelper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import static joshie.harvest.api.npc.gift.GiftCategory.*;

public class GiftsDaniel extends Gifts {
    @Override
    public Quality getQuality(ItemStack stack) {
        IGiftRegistry registry = HFApi.npc.getGifts();
        if (stack.getItem() == Items.ENDER_EYE || InventoryHelper.isOreName(stack, "enderpearl")) return Quality.AWESOME;
        else if (registry.isGiftType(stack, MINERAL)) return Quality.GOOD;
        else if (InventoryHelper.isOreName(stack, "fish")) return Quality.TERRIBLE;
        else if (registry.isGiftType(stack, FISH)) return Quality.BAD;
        else if (registry.isGiftType(stack, MAGIC)) return Quality.DISLIKE;
        else if (registry.isGiftType(stack, MONSTER)) return Quality.DECENT;
        else return super.getQuality(stack);
    }
}