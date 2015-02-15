package joshie.harvestmoon.blocks;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class BlockStone extends BlockHMBaseMeta {
    public static final int CAVE_WALL = 0;

    public BlockStone() {
        super(Material.rock);
    }

    @Override
    public int getMetaCount() {
        return 1;
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        return;
    }
}
