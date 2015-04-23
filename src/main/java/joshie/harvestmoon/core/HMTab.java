package joshie.harvestmoon.core;

import static joshie.harvestmoon.core.lib.HMModInfo.MODPATH;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import joshie.harvestmoon.api.HMApi;
import joshie.harvestmoon.api.core.ICreativeSorted;
import joshie.harvestmoon.init.HMBlocks;
import joshie.harvestmoon.init.HMBuildings;
import joshie.harvestmoon.init.HMCrops;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class HMTab extends CreativeTabs {
    public static HMTab tabFarming = new HMTab("farming");
    public static HMTab tabCooking = new HMTab("cooking");
    public static HMTab tabMining = new HMTab("mining");
    public static HMTab tabTown = new HMTab("town");
    
    private ItemStack icon = new ItemStack(Items.golden_hoe);

    public HMTab(String label) {
        super(label);
        setBackgroundImageName("hm.png");
        setNoTitle();
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
    
    @Override
    public boolean hasSearchBar() {
        return true;
    }
    
    @Override
    public int getSearchbarWidth() {
        return 69;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void displayAllReleventItems(List list) {
        super.displayAllReleventItems(list);
        Collections.sort(list, new Alphabetical());
    }
    
    private static class Alphabetical implements Comparator {
        @Override
        public int compare(Object o1, Object o2) {
            ItemStack stack1 = ((ItemStack) o1);
            ItemStack stack2 = ((ItemStack) o2);
            
            Item item1 = stack1.getItem();
            Item item2 = stack2.getItem();
            
            int value1 = 500;
            int value2 = 500;
            
            if (item1 instanceof ICreativeSorted) {
                value1 = ((ICreativeSorted)item1).getSortValue(stack1);
            }
            
            if (item2 instanceof ICreativeSorted) {
                value2 = ((ICreativeSorted)item2).getSortValue(stack2);
            }
            
            return value1 == value2 ? stack1.getDisplayName().compareTo(stack2.getDisplayName()) : value1 > value2? 1: -1;
        }
    }

    public void setStack(ItemStack stack) {
        this.icon = stack;
    }

    public static void init() {
        HMTab.tabFarming.setStack(HMCrops.tomato.getCropStack());
        HMTab.tabCooking.setStack(HMApi.COOKING.getMeal("cookies.chocolate"));
        HMTab.tabMining.setStack(new ItemStack(HMBlocks.stone, 1, 1));
        HMTab.tabTown.setStack(HMBuildings.supermarket.getPreview());
    }
}
