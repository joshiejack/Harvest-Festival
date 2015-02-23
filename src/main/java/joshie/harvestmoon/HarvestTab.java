package joshie.harvestmoon;

import static joshie.harvestmoon.lib.HMModInfo.MODPATH;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class HarvestTab extends CreativeTabs {
    public static HarvestTab tabGeneral = new HarvestTab("main");
    public ItemStack icon = new ItemStack(Items.golden_hoe);

    public HarvestTab(String label) {
        super(label);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getTranslatedTabLabel() {
        return MODPATH + ".tab." + this.getTabLabel();
    }

    @Override
    public ItemStack getIconItemStack() {
        return icon;
    }

    @Override
    public Item getTabIconItem() {
        return icon.getItem();
    }

    public void setStack(ItemStack stack) {
        this.icon = stack;
    }
}
