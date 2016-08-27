package joshie.harvest.npc.gift;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.npc.gift.GiftCategory;
import joshie.harvest.core.HFCore;
import joshie.harvest.crops.HFCrops;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class GiftsGoddess extends Gifts {
    @Override
    public Quality getQuality(ItemStack stack) {
        if (stack.getItem() == Item.getItemFromBlock(HFCore.FLOWERS) || stack.isItemEqual(HFCrops.STRAWBERRY.getCropStack())) {
            return Quality.AWESOME;
        }

        if (HFApi.npc.getGifts().isGiftType(stack, GiftCategory.PRETTY)) {
            return Quality.GOOD;
        }

        if (HFApi.npc.getGifts().isGiftType(stack, GiftCategory.CHEAP)) {
            return Quality.BAD;
        }

        return Quality.DECENT;
    }
}