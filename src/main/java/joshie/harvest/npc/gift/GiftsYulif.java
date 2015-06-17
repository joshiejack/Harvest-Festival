package joshie.harvest.npc.gift;

import static joshie.harvest.npc.gift.Gifts.Quality.AWESOME;
import static joshie.harvest.npc.gift.Gifts.Quality.DECENT;
import static joshie.harvest.npc.gift.Gifts.Quality.GOOD;
import joshie.harvest.crops.HFCrops;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class GiftsYulif extends Gifts {
    @Override
    public Quality getQuality(ItemStack stack) {
        if (stack.getItem() == Items.melon) {
            return AWESOME;
        }

        if (stack.getItem() == Items.sugar || stack.getItem() == Items.quartz || stack.getItem() == Item.getItemFromBlock(Blocks.cake)) {
            return GOOD;
        }

        if (HFCrops.corn.matches(stack) || HFCrops.pineapple.matches(stack)) {
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
