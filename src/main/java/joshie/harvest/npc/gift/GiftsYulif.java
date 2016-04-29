package joshie.harvest.npc.gift;

import joshie.harvest.crops.HFCrops;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import static joshie.harvest.npc.gift.Gifts.Quality.*;

public class GiftsYulif extends Gifts {
    @Override
    public Quality getQuality(ItemStack stack) {
        if (stack.getItem() == Items.MELON) {
            return AWESOME;
        }

        if (stack.getItem() == Items.SUGAR || stack.getItem() == Items.QUARTZ || stack.getItem() == Item.getItemFromBlock(Blocks.CAKE)) {
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