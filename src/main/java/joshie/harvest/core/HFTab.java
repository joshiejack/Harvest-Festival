package joshie.harvest.core;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.core.ISizeable.Size;
import joshie.harvest.core.util.ICreativeSorted;
import joshie.harvest.buildings.HFBuildings;
import joshie.harvest.core.lib.CreativeSort;
import joshie.harvest.core.lib.HFModInfo;
import joshie.harvest.core.util.HFLoader;
import joshie.harvest.crops.HFCrops;
import joshie.harvest.fishing.HFFishing;
import joshie.harvest.fishing.item.ItemFish.Fish;
import joshie.harvest.mining.HFMining;
import joshie.harvest.tools.HFTools;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static joshie.harvest.mining.item.ItemMaterial.Material.JUNK;


@HFLoader(priority = 0)
public class HFTab extends CreativeTabs {
    public static final HFTab TOWN = new HFTab("town");
    public static final HFTab MINING = new HFTab("mining");
    public static final HFTab FARMING = new HFTab("farming");
    public static final HFTab COOKING = new HFTab("cooking");
    public static final HFTab GATHERING = new HFTab("gathering");
    public static final HFTab FISHING = new HFTab("fishing");
    public static CreativeTabs[] tabs = new CreativeTabs[0];

    private ItemStack icon = new ItemStack(Items.GOLDEN_HOE);

    public HFTab(String label) {
        super(label);
        setBackgroundImageName("minecraft/textures/gui/container/creative_inventory/tab_item_search.png");
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
            } else if (HFApi.crops.getCropFromStack(stack1) != null) {
                value1 = CreativeSort.CROPS;
            }

            if (item2 instanceof ICreativeSorted) {
                value2 = ((ICreativeSorted) item2).getSortValue(stack2);
            } else if (HFApi.crops.getCropFromStack(stack2) != null) {
                value2 = CreativeSort.CROPS;
            }

            return value1 == value2 ? stack1.getDisplayName().compareTo(stack2.getDisplayName()) : value1 > value2 ? 1 : -1;
        }
    }

    public void setStack(ItemStack stack) {
        this.icon = stack;
    }

    public static void remap() {
        HFTab.TOWN.setStack(new ItemStack(HFBuildings.STRUCTURES, 1, 0));
        HFTab.MINING.setStack(HFMining.MATERIALS.getStackFromEnum(JUNK));
        HFTab.FARMING.setStack(HFCrops.STRAWBERRY.getCropStack(1));
        HFTab.COOKING.setStack(HFApi.cooking.getMeal("salad"));
        HFTab.GATHERING.setStack(new ItemStack(HFTools.AXE, 1, 0));
        HFTab.FISHING.setStack(Fish.GOLD.getStack(HFFishing.FISH, Size.LARGE));
    }
}