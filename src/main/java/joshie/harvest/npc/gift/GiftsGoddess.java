package joshie.harvest.npc.gift;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.npc.gift.GiftCategory;
import joshie.harvest.crops.HFCrops;
import net.minecraft.item.ItemStack;

public class GiftsGoddess extends Gifts {
    @Override
    public Quality getQuality(ItemStack stack) {
        if (stack.isItemEqual(HFCrops.STRAWBERRY.getCropStack(1))) {
            return Quality.AWESOME;
        }

        if (HFApi.npc.getGifts().isGiftType(stack, GiftCategory.CHEAP)) {
            return Quality.BAD;
        }

        if (HFApi.npc.getGifts().isGiftType(stack, GiftCategory.NATURE, GiftCategory.PRETTY)) {
            return Quality.GOOD;
        }

        return Quality.DECENT;
    }
}