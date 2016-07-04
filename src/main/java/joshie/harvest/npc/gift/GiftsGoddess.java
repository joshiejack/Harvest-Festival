package joshie.harvest.npc.gift;

import joshie.harvest.api.npc.gift.GiftCategory;
import joshie.harvest.blocks.HFBlocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class GiftsGoddess extends Gifts {
    @Override
    public Quality getQuality(ItemStack stack) {
        if (stack.getItem() == Item.getItemFromBlock(HFBlocks.FLOWERS)) {
            return Quality.AWESOME;
        }

        if (GiftRegistry.is(stack, GiftCategory.PRETTY)) {
            return Quality.GOOD;
        }

        if (GiftRegistry.is(stack, GiftCategory.CHEAP)) {
            return Quality.BAD;
        }

        return Quality.DECENT;
    }
}