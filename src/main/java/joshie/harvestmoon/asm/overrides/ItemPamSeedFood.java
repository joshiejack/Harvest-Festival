package joshie.harvestmoon.asm.overrides;

import java.util.List;
import java.util.WeakHashMap;

import joshie.harvestmoon.api.crops.ICrop;
import joshie.harvestmoon.core.config.Crops;
import joshie.harvestmoon.core.config.General;
import joshie.harvestmoon.core.helpers.CropHelper;
import joshie.harvestmoon.plugins.harvestcraft.HarvestCraft;
import joshie.harvestmoon.plugins.harvestcraft.HarvestCraftCrop;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemPamSeedFood {
    private static WeakHashMap<String, HarvestCraftCrop> cache = new WeakHashMap();
    public static boolean isLoaded = false;

    public static ICrop getCrop(ItemStack stack) {
        String unlocalized = stack.getUnlocalizedName();
        if (cache.containsKey(unlocalized)) {
            return cache.get(unlocalized);
        } else {
            for (HarvestCraftCrop crop : HarvestCraft.crops) {
                if (crop.matches(stack)) {
                    cache.put(unlocalized, crop);
                    return crop;
                }
            }
        }

        return null;
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
