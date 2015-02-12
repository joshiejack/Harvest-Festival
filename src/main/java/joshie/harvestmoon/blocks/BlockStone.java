package joshie.harvestmoon.blocks;

import net.minecraft.block.material.Material;

public class BlockStone extends BlockHMBaseMeta {
    public static final int CAVE_WALL = 0;
    
    public BlockStone() {
        super(Material.rock);
    }

    @Override
    public int getMetaCount() {
        return 1;
    }
}
