package joshie.harvest.asm.overrides;

import java.util.List;

import joshie.harvest.api.core.IShippable;
import joshie.harvest.api.crops.ICrop;
import joshie.harvest.api.crops.ICropProvider;
import joshie.harvest.core.lib.CreativeSort;
import joshie.harvest.core.util.ICreativeSorted;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemWheat extends Item implements IShippable, ICropProvider, ICreativeSorted {
    public ItemWheat() {}
    
    @Override
    public int getSortValue(ItemStack stack) {
        return CreativeSort.CROPS;
    }
    
    @Override
    public ICrop getCrop(ItemStack stack) {
        return ItemSeedFood.getCrop(stack);
    }

    @Override
    public ItemWheat setUnlocalizedName(String string) {
        return (ItemWheat) super.setUnlocalizedName(string);
    }

    @Override
    public ItemWheat setCreativeTab(CreativeTabs tab) {
        return (ItemWheat) super.setCreativeTab(tab);
    }

    @Override
    public ItemWheat setTextureName(String name) {
        return (ItemWheat) super.setTextureName(name);
    }

    @Override
    public long getSellValue(ItemStack stack) {
        return ItemSeedFood.getSellValue(stack);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return ItemSeedFood.getItemStackDisplayName(stack);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        ItemSeedFood.getSubItems(item, tab, list);
    }
}
