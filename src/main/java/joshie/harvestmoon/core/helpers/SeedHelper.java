package joshie.harvestmoon.core.helpers;

import joshie.harvestmoon.api.crops.ICrop;
import joshie.harvestmoon.init.HMConfiguration;
import joshie.harvestmoon.init.HMCrops;
import joshie.harvestmoon.init.HMItems;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class SeedHelper {
    public static ICrop getCropFromSeed(ItemStack stack) {
        if (!stack.hasTagCompound()) {
            return HMCrops.null_crop;
        }
        
        String unlocalized = stack.getTagCompound().getString("UnlocalizedSeedName");
        return HMConfiguration.mappings.getCrop(unlocalized);
    }

    public static int getQualityFromSeed(ItemStack stack) {
        if (!stack.hasTagCompound()) {
            return 0;
        }
        
        return stack.getTagCompound().getInteger("SeedQuality");
    }

    public static ItemStack getSeedsFromCrop(ICrop crop) {
        ItemStack seeds = new ItemStack(HMItems.seeds);
        seeds.setTagCompound(new NBTTagCompound());
        seeds.getTagCompound().setString("UnlocalizedSeedName", crop.getUnlocalizedName());
        seeds.getTagCompound().setInteger("SeedQuality", 0);
        return seeds;
    }

    public static ItemStack getSeedsFromCropWithQuality(ICrop crop, int quality) {
        ItemStack seeds = new ItemStack(HMItems.seeds);
        seeds.setTagCompound(new NBTTagCompound());
        seeds.getTagCompound().setString("UnlocalizedSeedName", crop.getUnlocalizedName());
        seeds.getTagCompound().setInteger("SeedQuality", quality);
        return seeds;
    }
}
