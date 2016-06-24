package joshie.harvest.items;

import joshie.harvest.api.core.ICreativeSorted;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.util.base.ItemHFBaseMeta;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

import static net.minecraft.util.text.TextFormatting.AQUA;
import static net.minecraft.util.text.TextFormatting.WHITE;

public class ItemGeneral extends ItemHFBaseMeta implements ICreativeSorted {
    public static final int BLUE_FEATHER = 0;
    public static final int JUNK_ORE = 1;

    @Override
    public int getMetaCount() {
        return 22;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return getName(stack);
    }

    @Override
    public String getName(ItemStack stack) {
        switch (stack.getItemDamage()) {
            case BLUE_FEATHER:
                return "feather_blue";
            default:
                return "invalid";
        }
    }

    @Override
    public boolean isValidTab(CreativeTabs tab, int meta) {
        switch (meta) {
            case BLUE_FEATHER:
                return tab == HFTab.TOWN;
            default:
                return false;
        }
    }





    @Override
    public int getSortValue(ItemStack stack) {
        if (stack.getItemDamage() == BLUE_FEATHER) return 1;
        return 102;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        switch (stack.getItemDamage()) {
            case BLUE_FEATHER:
                return AQUA + super.getItemStackDisplayName(stack);
            default:
                return WHITE + super.getItemStackDisplayName(stack);
        }
    }
}