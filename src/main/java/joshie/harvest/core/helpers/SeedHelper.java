package joshie.harvest.core.helpers;

import joshie.harvest.api.crops.ICrop;
import joshie.harvest.crops.Crop;
import joshie.harvest.crops.CropRegistry;
import joshie.harvest.crops.HFCrops;
import net.minecraft.item.ItemStack;

public class SeedHelper {
    public static ICrop getCropFromSeed(ItemStack stack) {
        Crop crop = CropRegistry.REGISTRY.getObjectById(stack.getItemDamage());
        return crop == null ? HFCrops.NULL_CROP : crop;
    }

    public static ItemStack getSeedsFromCrop(Crop crop) {
        return new ItemStack(HFCrops.SEEDS, 1, CropRegistry.REGISTRY.getId(crop));
    }
}