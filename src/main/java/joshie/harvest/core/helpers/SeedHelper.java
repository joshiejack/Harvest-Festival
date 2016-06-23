package joshie.harvest.core.helpers;

import joshie.harvest.crops.Crop;
import joshie.harvest.crops.CropRegistry;
import joshie.harvest.crops.HFCrops;
import net.minecraft.item.ItemStack;

public class SeedHelper {
    public static Crop getCropFromSeed(ItemStack stack) {
        return CropRegistry.REGISTRY.getObjectById(stack.getItemDamage());
    }

    public static ItemStack getSeedsFromCrop(Crop crop) {
        return new ItemStack(HFCrops.SEEDS, 1, CropRegistry.REGISTRY.getId(crop));
    }
}