package joshie.harvest.core.util.base;

import joshie.harvest.core.HFTab;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public abstract class ItemHFBaseEnum<E extends Enum<E>> extends ItemHFBase {
    protected final E[] values;
    public ItemHFBaseEnum(Class<E> clazz) {
        super();
        values = clazz.getEnumConstants();
        setHasSubtypes(true);
    }

    public ItemHFBaseEnum(CreativeTabs tab, Class<E> clazz) {
        super(tab);
        values = clazz.getEnumConstants();
        setHasSubtypes(true);
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }

    public boolean isValidTab(CreativeTabs tab, E e) {
        return tab == getCreativeTab();
    }

    @Override
    public CreativeTabs[] getCreativeTabs() {
        return new CreativeTabs[]{HFTab.FARMING, HFTab.COOKING, HFTab.MINING, HFTab.TOWN};
    }

    public boolean isActive(E e) {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> list) {
        for (E e: values) {
            if (isActive(e) && isValidTab(tab, e)) {
                list.add(new ItemStack(item, 1, e.ordinal()));
            }
        }
    }
}