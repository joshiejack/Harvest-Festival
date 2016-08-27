package joshie.harvest.core.helpers;

import joshie.harvest.crops.Crop;
import joshie.harvest.crops.CropRegistry;
import net.minecraft.item.ItemStack;

public class SeedHelper {
    public static Crop getCropFromSeed(ItemStack stack) {
        return CropRegistry.REGISTRY.getObjectById(stack.getItemDamage());
    }
}