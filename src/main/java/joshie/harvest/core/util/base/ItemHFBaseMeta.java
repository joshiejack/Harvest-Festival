package joshie.harvest.core.util.base;

import joshie.harvest.core.HFTab;
import joshie.harvest.core.util.generic.IHasMetaItem;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public abstract class ItemHFBaseMeta extends ItemHFBase implements IHasMetaItem {

    public ItemHFBaseMeta() {
        super();
        setHasSubtypes(true);
    }

    public ItemHFBaseMeta(CreativeTabs tab) {
        super(tab);
        setHasSubtypes(true);
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }

    public boolean isValidTab(CreativeTabs tab, int meta) {
        return tab == getCreativeTab();
    }

    @Override
    public CreativeTabs[] getCreativeTabs() {
        return new CreativeTabs[]{HFTab.FARMING, HFTab.COOKING, HFTab.MINING, HFTab.TOWN, HFTab.GATHERING};
    }

    public boolean isActive(int damage) {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> list) {
        for (int i = 0; i < getMetaCount(); i++) {
            if (isActive(i) && isValidTab(tab, i)) {
                list.add(new ItemStack(item, 1, i));
            }
        }
    }
}