package joshie.harvest.asm.overrides;

import joshie.harvest.api.crops.ICrop;
import joshie.harvest.core.config.HFConfig;
import joshie.harvest.core.lib.CreativeSort;
import joshie.harvest.crops.HFCrops;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemSeedFood {
    public static ICrop getCrop(ItemStack stack) {
        ICrop crop = null;
        Item item = stack.getItem();
        if (item == Items.CARROT && HFConfig.asm.CARROT_OVERRIDE) {
            crop = HFCrops.carrot;
        } else if (item == Items.potato && HFConfig.asm.POTATO_OVERRIDE) {
            crop = HFCrops.potato;
        } else if (item == Items.WHEAT && HFConfig.asm.WHEAT_OVERRIDE) {
            crop = HFCrops.wheat;
        } else if (item == Item.getItemFromBlock(Blocks.pumpkin) && HFConfig.asm.PUMPKIN_OVERRIDE) {
            crop = HFCrops.pumpkin;
        } else if (item == Items.MELON && HFConfig.asm.WATERMELON_OVERRIDE) {
            crop = HFCrops.watermelon;
        }

        return crop;
    }

    public static long getSellValue(ItemStack stack) {
        ICrop crop = getCrop(stack);
        return crop == null ? 0 : crop.getSellValue();
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
