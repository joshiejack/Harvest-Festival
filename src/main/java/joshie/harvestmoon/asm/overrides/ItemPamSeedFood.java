package joshie.harvestmoon.asm.overrides;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import joshie.harvestmoon.api.crops.ICrop;
import joshie.harvestmoon.core.config.Crops;
import joshie.harvestmoon.core.config.General;
import joshie.harvestmoon.core.helpers.CropHelper;
import joshie.harvestmoon.plugins.harvestcraft.HarvestCraft;
import joshie.harvestmoon.plugins.harvestcraft.HarvestCraftCrop;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemPamSeedFood {
    private static Cache<String, HarvestCraftCrop> cache = CacheBuilder.newBuilder().maximumSize(1000).build();
    public static boolean isLoaded = false;

    public static ICrop getCrop(final ItemStack stack) {
        try {
            final String unlocalized = stack.getUnlocalizedName();
            return cache.get(unlocalized, new Callable<HarvestCraftCrop>() {
                @Override
                public HarvestCraftCrop call() {
                    for (HarvestCraftCrop crop : HarvestCraft.crops) {
                        if (crop.matches(stack)) {
                            cache.put(unlocalized, crop);
                            return crop;
                        }
                    }

                    return null; //If we failed to find the crop, return null
                }
            });
        } catch (ExecutionException e) {
            return null; //Return null on failure
        }
    }

    public static long getSellValue(ItemStack stack) {
        ICrop crop = getCrop(stack);
        if (crop == null || crop.isStatic()) return 0;
        double quality = (1 + CropHelper.getCropQualityFromDamage(stack.getItemDamage()) * General.SELL_QUALITY_MODIFIER);
        return (long) (quality * crop.getSellValue());
    }

    public static int getRating(ItemStack stack) {
        ICrop crop = getCrop(stack);
        if (crop == null || crop.isStatic()) return -1;
        else return CropHelper.getCropQualityFromDamage(stack.getItemDamage()) / 10;
    }

    @SideOnly(Side.CLIENT)
    public static void getSubItems(Item item, CreativeTabs tab, List list) {
        if (getCrop(new ItemStack(item)) != null) {
            for (int j = 0; j < 100; j += Crops.CROP_QUALITY_LOOP) {
                list.add(new ItemStack(item, 1, (j * 100)));
            }
        } else list.add(new ItemStack(item, 1, 0));
    }
}
