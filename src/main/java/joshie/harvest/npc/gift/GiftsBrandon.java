package joshie.harvest.npc.gift;

import joshie.harvest.api.npc.gift.GiftCategory;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class GiftsBrandon extends Gifts {
    @Override
    public Quality getQuality(ItemStack stack) {
        if (stack.getItem() == Items.IRON_PICKAXE) {
            return Quality.AWESOME;
        }

        if (isGiftType(stack, GiftCategory.MINING)) {
            return Quality.GOOD;
        }

        if (isGiftType(stack, GiftCategory.NATURE)) {
            return Quality.BAD;
        }

        return Quality.DECENT;
    }
}