package joshie.harvestmoon.items;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemAnimal extends ItemHMMeta {
    public static final int CHICKEN = 0;
    public static final int SHEEP = 1;
    public static final int COW = 2;

    @Override
    public String getName(ItemStack stack) {
        switch (stack.getItemDamage()) {
            case CHICKEN:
                return "chicken";
            case SHEEP:
                return "sheep";
            case COW:
                return "cow";
            default:
                return "null";
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister register) {
        return;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        return;
    }

    @Override
    public int getMetaCount() {
        return 0;
    }
}
