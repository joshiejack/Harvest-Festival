package joshie.harvestmoon.base;

import java.util.List;
import java.util.Random;

import joshie.harvestmoon.util.generic.IHasMetaBlock;
import joshie.harvestmoon.util.generic.IHasMetaItem;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class BlockBaseMeta extends BlockBase implements IHasMetaBlock {
    @SideOnly(Side.CLIENT)
    public IIcon[] icons;
    protected String prefix;
    
    protected BlockBaseMeta(Material material, String mod, CreativeTabs tab) {
        super(material, mod, tab);
        for (int i = 0; i < getMetaCount(); i++) {
            setHarvestLevel(getToolType(i), getToolLevel(i), i);
        }
    }
    
    public abstract String getToolType(int meta);
    public abstract int getToolLevel(int meta);

    @Override
    public Class<? extends ItemBlock> getItemClass() {
        return null;
    }
    
    @Override
    public Item getItemDropped(int meta, Random rand, int side) {
        return !doesDrop(meta) ? null : super.getItemDropped(meta, rand, side);
    }
    
    public boolean doesDrop(int meta) {
        return true;
    }
    
    @Override
    public int damageDropped(int damage) {
        return damage;
    }
    
    @Override
    public IIcon getIcon(int side, int meta) {
        if(blockIcon != null) return blockIcon;
        else if (icons == null) return Blocks.stone.getIcon(side, meta);
        else {
            IIcon icon = icons[meta < icons.length ? meta : 0];
            return icon != null? icon: Blocks.stone.getIcon(side, meta);
        }
    }
    
    protected String getName(int i) {
        return ((IHasMetaItem) Item.getItemFromBlock(this)).getName(new ItemStack(this, 1, i));
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        String name = prefix != null ? prefix : "";
        icons = new IIcon[getMetaCount()];

        for (int i = 0; i < icons.length; i++) {
            icons[i] = iconRegister.registerIcon(mod + ":" + name + getName(i));
        }
    }

    public boolean isValidTab(CreativeTabs tab, int meta) {
        return tab == getCreativeTabToDisplayOn();
    }

    public boolean isActive(int damage) {
        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        for (int i = 0; i < getMetaCount(); i++) {
            if (isActive(i) && isValidTab(tab, i)) {
                list.add(new ItemStack(item, 1, i));
            }
        }
    }
}
