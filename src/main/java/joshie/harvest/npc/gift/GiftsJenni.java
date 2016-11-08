package joshie.harvest.npc.gift;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.npc.gift.IGiftRegistry;
import joshie.harvest.core.helpers.InventoryHelper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import static joshie.harvest.api.npc.gift.GiftCategory.*;

public class GiftsJenni extends Gifts {
    @Override
    public Quality getQuality(ItemStack stack) {
        IGiftRegistry registry = HFApi.npc.getGifts();
        if (stack.getItem() == Items.CARROT_ON_A_STICK || InventoryHelper.isOreName(stack, "cropCarrot")) return Quality.AWESOME;
        else if (registry.isGiftType(stack, VEGETABLE)) return Quality.GOOD;
        else if (stack.getItem() == Items.FLINT || InventoryHelper.startsWith(stack, "ingot")) return Quality.TERRIBLE;
        else if (registry.isGiftType(stack, MINERAL, MEAT)) return Quality.BAD;
        else if (registry.isGiftType(stack, JUNK, BUILDING)) return Quality.DISLIKE;
        else return super.getQuality(stack);
    }
}