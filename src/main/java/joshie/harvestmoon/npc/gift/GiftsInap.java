package joshie.harvestmoon.npc.gift;

import static joshie.harvestmoon.npc.gift.Gifts.Quality.AWESOME;
import static joshie.harvestmoon.npc.gift.Gifts.Quality.DECENT;
import static joshie.harvestmoon.npc.gift.Gifts.Quality.GOOD;
import joshie.harvestmoon.crops.Crop;
import joshie.harvestmoon.helpers.CropHelper;
import joshie.harvestmoon.init.HMCrops;
import joshie.harvestmoon.init.HMItems;
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

        if (stack.getItem() == HMItems.crops) {
            Crop crop = CropHelper.getCropFromStack(stack);
            return crop == HMCrops.corn || crop == HMCrops.pineapple ? GOOD : DECENT;
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
