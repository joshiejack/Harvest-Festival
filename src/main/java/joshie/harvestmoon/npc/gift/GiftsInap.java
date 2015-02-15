package joshie.harvestmoon.npc.gift;

import static joshie.harvestmoon.npc.gift.Gifts.GiftQuality.AWESOME;
import static joshie.harvestmoon.npc.gift.Gifts.GiftQuality.DECENT;
import static joshie.harvestmoon.npc.gift.Gifts.GiftQuality.GOOD;
import joshie.harvestmoon.crops.Crop;
import joshie.harvestmoon.helpers.CropHelper;
import joshie.harvestmoon.init.HMCrops;
import joshie.harvestmoon.init.HMItems;
import joshie.harvestmoon.items.ItemGeneral;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class GiftsInap extends Gifts {
    @Override
    public GiftQuality getValue(ItemStack stack) {
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

        if (stack.getItem() == HMItems.general && stack.getItemDamage() == ItemGeneral.BAMBOO_SHOOT) {
            return GOOD;
        }

        return DECENT;
    }
}
