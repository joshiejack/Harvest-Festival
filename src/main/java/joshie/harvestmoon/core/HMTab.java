package joshie.harvestmoon.core;

import static joshie.harvestmoon.core.lib.HMModInfo.MODPATH;
import joshie.harvestmoon.init.HMCrops;
import joshie.harvestmoon.init.HMItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class HMTab extends CreativeTabs {
    public static HMTab tabGeneral = new HMTab("main");
    private ItemStack icon = new ItemStack(Items.golden_hoe);

    public HMTab(String label) {
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

    public static void init() {
        HMTab.tabGeneral.setStack(HMCrops.tomato.getCropStack());
    }
}
