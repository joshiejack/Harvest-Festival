package joshie.harvestmoon.asm.overrides;

import java.util.List;

import joshie.harvestmoon.api.crops.ICrop;
import joshie.harvestmoon.core.config.Crops;
import joshie.harvestmoon.core.config.General;
import joshie.harvestmoon.core.helpers.CropHelper;
import joshie.harvestmoon.core.lib.CreativeSort;
import joshie.harvestmoon.init.HMConfiguration;
import joshie.harvestmoon.init.HMCrops;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemSeedFood {
    public static ICrop getCrop(ItemStack stack) {
        ICrop crop = null;
        Item item = stack.getItem();
        if (item == Items.carrot && HMConfiguration.vanilla.CARROT_OVERRIDE) {
            crop = HMCrops.carrot;
        } else if (item == Items.potato && HMConfiguration.vanilla.POTATO_OVERRIDE) {
            crop = HMCrops.potato;
        } else if (item == Items.wheat && HMConfiguration.vanilla.WHEAT_OVERRIDE) {
            crop = HMCrops.wheat;
        } else if (item == Item.getItemFromBlock(Blocks.pumpkin) && HMConfiguration.vanilla.PUMPKIN_OVERRIDE) {
            crop = HMCrops.pumpkin;
        }

        return crop;
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

    public static String getItemStackDisplayName(ItemStack stack) {
        ICrop crop = getCrop(stack);
        if (crop == null) return ("" + StatCollector.translateToLocal(stack.getItem().getUnlocalizedNameInefficiently(stack) + ".name")).trim();
        else return crop.getLocalizedName(true);
    }
    
    public static int getSortValue() {
        return CreativeSort.CROPS;
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
