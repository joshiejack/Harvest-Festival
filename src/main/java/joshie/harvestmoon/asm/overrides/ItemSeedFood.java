package joshie.harvestmoon.asm.overrides;

import static joshie.harvestmoon.core.helpers.CropHelper.getCropQuality;
import static joshie.harvestmoon.core.helpers.CropHelper.isGiant;

import java.util.List;

import joshie.harvestmoon.api.interfaces.IShippable;
import joshie.harvestmoon.crops.Crop;
import joshie.harvestmoon.init.HMConfiguration;
import joshie.harvestmoon.init.HMCrops;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemSeedFood {
    private static Crop getCrop(ItemStack stack) {
        Crop crop = null;
        Item item = stack.getItem();
        if (item == Items.carrot && HMConfiguration.vanilla.CARROT_OVERRIDE) {
            crop = HMCrops.carrot;
        } else if (item == Items.potato && HMConfiguration.vanilla.POTATO_OVERRIDE) {
            crop = HMCrops.potato;
        } else if (item == Items.wheat && HMConfiguration.vanilla.WHEAT_OVERRIDE) {
            crop = HMCrops.wheat;
        }

        return crop;
    }

    public static long getSellValue(ItemStack stack) {
        Crop crop = getCrop(stack);
        if (crop == null || crop.isStatic()) return 0;
        boolean isGiant = isGiant(stack.getItemDamage());
        double quality = 1 + (getCropQuality(stack.getItemDamage()) * IShippable.SELL_QUALITY_MODIFIER);
        long ret = (int) quality * crop.getSellValue();
        return (int) (isGiant ? ret * 10 : ret);
    }

    public static int getRating(ItemStack stack) {
        Crop crop = getCrop(stack);
        if (crop == null || crop.isStatic()) return -1;
        else return getCropQuality(stack.getItemDamage()) / 10;
    }

    public static String getItemStackDisplayName(ItemStack stack) {
        Crop crop = getCrop(stack);
        if (crop == null) return ("" + StatCollector.translateToLocal(stack.getItem().getUnlocalizedNameInefficiently(stack) + ".name")).trim();
        else return crop.getCropName(true, isGiant(stack.getItemDamage()));
    }

    @SideOnly(Side.CLIENT)
    public static void getSubItems(Item item, CreativeTabs tab, List list) {
        if (getCrop(new ItemStack(item)) != null) {
            for (int j = 0; j < 100; j += 100) {
                list.add(new ItemStack(item, 1, (j * 100)));
            }
        } else list.add(new ItemStack(item, 1, 0));
    }
}
