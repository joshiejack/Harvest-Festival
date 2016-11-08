package joshie.harvest.npc.gift;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.npc.gift.IGiftRegistry;
import joshie.harvest.core.helpers.InventoryHelper;
import joshie.harvest.crops.HFCrops;
import joshie.harvest.crops.item.ItemCrop.Crops;
import net.minecraft.item.ItemStack;

import static joshie.harvest.api.npc.gift.GiftCategory.*;

public class GiftsFenn extends Gifts {
    private boolean isPinkCrop(Crops crops) {
        return crops == Crops.SWEET_POTATO || crops == Crops.BEETROOT || crops == Crops.GRAPE || crops == Crops.PEACH || crops == Crops.TURNIP;
    }

    @Override
    public Quality getQuality(ItemStack stack) {
        IGiftRegistry registry = HFApi.npc.getGifts();
        if (InventoryHelper.isOreName(stack, "vine") || InventoryHelper.isOreName(stack, "treeLeaves")) return Quality.AWESOME;
        else if (registry.isGiftType(stack, PLANT)) return Quality.GOOD;
        else if (stack.getItem() == HFCrops.CROP && isPinkCrop(HFCrops.CROP.getEnumFromStack(stack))) return Quality.TERRIBLE;
        else if (registry.isGiftType(stack, SWEET, COOKING)) return Quality.BAD;
        else if (registry.isGiftType(stack, GEM, MONSTER, JUNK)) return Quality.DISLIKE;
        else return super.getQuality(stack);
    }
}
