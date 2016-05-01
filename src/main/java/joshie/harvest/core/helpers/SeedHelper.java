package joshie.harvest.core.helpers;

import joshie.harvest.api.crops.ICrop;
import joshie.harvest.core.config.HFConfig;
import joshie.harvest.crops.HFCrops;
import joshie.harvest.items.HFItems;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class SeedHelper {
    public static ICrop getCropFromSeed(ItemStack stack) {
        if (!stack.hasTagCompound()) {
            return HFCrops.null_crop;
        }

        String unlocalized = stack.getTagCompound().getString("UnlocalizedSeedName");
        return HFConfig.mappings.getCrop(unlocalized);
    }

    public static ItemStack getSeedsFromCrop(ICrop crop) {
        ItemStack seeds = new ItemStack(HFItems.SEEDS);
        seeds.setTagCompound(new NBTTagCompound());
        seeds.getTagCompound().setString("UnlocalizedSeedName", crop.getUnlocalizedName());
        return seeds;
    }
}