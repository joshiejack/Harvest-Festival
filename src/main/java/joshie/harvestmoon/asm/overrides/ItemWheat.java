package joshie.harvestmoon.asm.overrides;

import java.util.List;

import joshie.harvestmoon.api.interfaces.ICropProvider;
import joshie.harvestmoon.api.interfaces.IRateable;
import joshie.harvestmoon.api.interfaces.IShippable;
import joshie.harvestmoon.crops.Crop;
import joshie.harvestmoon.init.HMCrops;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemWheat extends Item implements IRateable, IShippable, ICropProvider {
    public ItemWheat() {

    }
    
    @Override
    public Crop getCrop(ItemStack stack) {
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
    public int getRating(ItemStack stack) {
        return ItemSeedFood.getRating(stack);
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
