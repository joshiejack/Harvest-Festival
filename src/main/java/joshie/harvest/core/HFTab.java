package joshie.harvest.core;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.core.ICreativeSorted;
import joshie.harvest.items.HFItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static joshie.harvest.core.lib.HFModInfo.MODPATH;

public class HFTab extends CreativeTabs {
    public static HFTab tabTown = new HFTab("town");
    public static HFTab tabMining = new HFTab("mining");
    public static HFTab tabFarming = new HFTab("farming");
    public static HFTab tabCooking = new HFTab("cooking");

    private ItemStack icon = new ItemStack(Items.golden_hoe);

    public HFTab(String label) {
        super(label);
        setBackgroundImageName("hf.png");
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

    public static void preInit() {
        HFTab.tabTown.setStack(new ItemStack(HFItems.structures, 0, 0));
        /*
        HFTab.tabMining.setStack(new ItemStack(HFBlocks.stone, 0, 0));
        */
        HFTab.tabFarming.setStack(new ItemStack(HFItems.hoe, 1, 0));
        HFTab.tabCooking.setStack(HFApi.COOKING.getMeal("salad"));
    }
}
