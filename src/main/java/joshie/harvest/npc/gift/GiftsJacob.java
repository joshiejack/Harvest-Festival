package joshie.harvest.npc.gift;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.npc.gift.GiftCategory;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemStack;

public class GiftsJacob extends Gifts {
    @Override
    public Quality getQuality(ItemStack stack) {
        if (stack.getItem() instanceof ItemFishingRod) {
            return Quality.AWESOME;
        }

        if (HFApi.npc.getGifts().isGiftType(stack, GiftCategory.WATERY)) {
            return Quality.GOOD;
        }

        if (HFApi.npc.getGifts().isGiftType(stack, GiftCategory.MINING)) {
            return Quality.BAD;
        }

        return Quality.DECENT;
    }
}