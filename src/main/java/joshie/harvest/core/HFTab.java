package joshie.harvest.core;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.core.ICreativeSorted;
import joshie.harvest.blocks.HFBlocks;
import joshie.harvest.core.lib.HFModInfo;
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

public class HFTab extends CreativeTabs {
    public static final HFTab TOWN = new HFTab("town");
    public static final HFTab MINING = new HFTab("mining");
    public static final HFTab FARMING = new HFTab("farming");
    public static final HFTab COOKING = new HFTab("cooking");

    private ItemStack icon = new ItemStack(Items.GOLDEN_HOE);

    public HFTab(String label) {
        super(label);
        setBackgroundImageName("hf.png");
        setNoTitle();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getTranslatedTabLabel() {
        return HFModInfo.MODID + ".tab." + this.getTabLabel();
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
    public void displayAllRelevantItems(List<ItemStack> list) {
        super.displayAllRelevantItems(list);
        Collections.sort(list, new Alphabetical());
    }

    private static class Alphabetical implements Comparator<Object> {
        @Override
        public int compare(Object o1, Object o2) {
            ItemStack stack1 = ((ItemStack) o1);
            ItemStack stack2 = ((ItemStack) o2);

            Item item1 = stack1.getItem();
            Item item2 = stack2.getItem();

            int value1 = 500;
            int value2 = 500;

            if (item1 instanceof ICreativeSorted) {
                value1 = ((ICreativeSorted) item1).getSortValue(stack1);
            }

            if (item2 instanceof ICreativeSorted) {
                value2 = ((ICreativeSorted) item2).getSortValue(stack2);
            }

            return value1 == value2 ? stack1.getDisplayName().compareTo(stack2.getDisplayName()) : value1 > value2 ? 1 : -1;
        }
    }

    public void setStack(ItemStack stack) {
        this.icon = stack;
    }

    public static void preInit() {
        HFTab.TOWN.setStack(new ItemStack(HFItems.STRUCTURES, 1, 0));
        HFTab.MINING.setStack(new ItemStack(HFBlocks.STONE, 1, 0));
        HFTab.FARMING.setStack(new ItemStack(HFItems.HOE, 1, 0));
        HFTab.COOKING.setStack(HFApi.COOKING.getMeal("salad"));
    }
}