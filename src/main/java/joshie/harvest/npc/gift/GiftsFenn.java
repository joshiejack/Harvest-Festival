package joshie.harvest.npc.gift;

import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class GiftsFenn extends Gifts {
    @Override
    public Quality getQuality(ItemStack stack) {
        if (stack.getItem() == Item.getItemFromBlock(Blocks.VINE)) {
            return Quality.AWESOME;
        }

        if (is(stack, Category.NATURE)) {
            return Quality.GOOD;
        }

        if (is(stack, Category.PRETTY)) {
            return Quality.BAD;
        }

        return Quality.DECENT;
    }
}
