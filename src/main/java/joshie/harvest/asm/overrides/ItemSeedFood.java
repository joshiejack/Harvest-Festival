package joshie.harvest.asm.overrides;

import java.util.List;

import joshie.harvest.api.crops.ICrop;
import joshie.harvest.core.config.General;
import joshie.harvest.core.lib.CreativeSort;
import joshie.harvest.init.HFConfig;
import joshie.harvest.init.HFCrops;
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
        if (item == Items.carrot && HFConfig.vanilla.CARROT_OVERRIDE) {
            crop = HFCrops.carrot;
        } else if (item == Items.potato && HFConfig.vanilla.POTATO_OVERRIDE) {
            crop = HFCrops.potato;
        } else if (item == Items.wheat && HFConfig.vanilla.WHEAT_OVERRIDE) {
            crop = HFCrops.wheat;
        } else if (item == Item.getItemFromBlock(Blocks.pumpkin) && HFConfig.vanilla.PUMPKIN_OVERRIDE) {
            crop = HFCrops.pumpkin;
        } else if (item == Items.melon && HFConfig.vanilla.WATERMELON_OVERRIDE) {
            crop = HFCrops.watermelon;
        }

        return crop;
    }

    public static long getSellValue(ItemStack stack) {
        ICrop crop = getCrop(stack);
        return crop == null ? 0 : (long) (General.SELL_QUALITY_MODIFIER * crop.getSellValue());
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
        list.add(new ItemStack(item, 1, 0));
    }
}
