package harvestmoon;

import static harvestmoon.lib.ModInfo.MODPATH;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class HarvestTab extends CreativeTabs {
    public static HarvestTab hm = new HarvestTab("main");
    //public static HarvestTab crops = new HarvestTab("crops");
    //public static HarvestTab seeds = new HarvestTab("seeds");
    public ItemStack icon = new ItemStack(Items.wheat_seeds);
    public HarvestTab(String label) {
        super(label);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public String getTranslatedTabLabel() {
        return MODPATH + ".tab." + this.getTabLabel();
    }

    @Override
    public ItemStack getIconItemStack () {
        return icon;
    }

    @Override
    public Item getTabIconItem() {
        return icon.getItem();
    }
}
