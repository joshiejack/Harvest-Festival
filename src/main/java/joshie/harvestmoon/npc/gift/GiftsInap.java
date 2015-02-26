package joshie.harvestmoon.npc.gift;

import static joshie.harvestmoon.npc.gift.Gifts.Quality.AWESOME;
import static joshie.harvestmoon.npc.gift.Gifts.Quality.DECENT;
import static joshie.harvestmoon.npc.gift.Gifts.Quality.GOOD;
import joshie.harvestmoon.init.HMCrops;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class GiftsInap extends Gifts {
    @Override
    public Quality getQuality(ItemStack stack) {
        if (stack.getItem() == Items.melon) {
            return AWESOME;
        }

        if (stack.getItem() == Items.sugar || stack.getItem() == Items.quartz || stack.getItem() == Item.getItemFromBlock(Blocks.cake)) {
            return GOOD;
        }

        if (HMCrops.corn.matches(stack) || HMCrops.pineapple.matches(stack)) {
            return GOOD;
        }

        if (is(stack, Category.CONSTRUCTION)) {
            return Quality.GOOD;
        }

        if (is(stack, Category.FARMING)) {
            return Quality.BAD;
        }

        return DECENT;
    }
}
