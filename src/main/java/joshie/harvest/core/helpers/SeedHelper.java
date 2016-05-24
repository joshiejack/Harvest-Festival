package joshie.harvest.core.helpers;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.crops.ICrop;
import joshie.harvest.crops.HFCrops;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class SeedHelper {
    public static ICrop getCropFromSeed(ItemStack stack) {
        if (!stack.hasTagCompound()) {
            return HFCrops.NULL_CROP;
        }

        ResourceLocation resource = new ResourceLocation(stack.getTagCompound().getString("ResourceSeedName"));
        return HFApi.crops.getCrop(resource);
    }

    public static ItemStack getSeedsFromCrop(ICrop crop) {
        ItemStack seeds = new ItemStack(HFCrops.SEEDS);
        seeds.setTagCompound(new NBTTagCompound());
        seeds.getTagCompound().setString("ResourceSeedName", crop.getResource().toString());
        return seeds;
    }
}