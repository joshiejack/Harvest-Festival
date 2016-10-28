package joshie.harvest.npc.gift;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.npc.gift.GiftCategory;
import joshie.harvest.tools.ToolHelper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class GiftsCandice extends Gifts {
    @Override
    public Quality getQuality(ItemStack stack) {
        if (stack.getItem() == Items.MILK_BUCKET || ToolHelper.isMilk(stack)) {
            return Quality.AWESOME;
        }

        if (HFApi.npc.getGifts().isGiftType(stack, GiftCategory.ANIMALS)) {
            return Quality.GOOD;
        }

        if (HFApi.npc.getGifts().isGiftType(stack, GiftCategory.COOKING)) {
            return Quality.BAD;
        }

        return Quality.DECENT;
    }
}
