package joshie.harvest.core.helpers;

import joshie.harvest.api.crops.ICrop;
import joshie.harvest.init.HFConfig;
import joshie.harvest.init.HFCrops;
import joshie.harvest.init.HFItems;
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
        ItemStack seeds = new ItemStack(HFItems.seeds);
        seeds.setTagCompound(new NBTTagCompound());
        seeds.getTagCompound().setString("UnlocalizedSeedName", crop.getUnlocalizedName());
        return seeds;
    }
}
