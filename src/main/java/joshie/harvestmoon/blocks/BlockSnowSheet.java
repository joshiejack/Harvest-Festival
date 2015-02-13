package joshie.harvestmoon.blocks;

import net.minecraft.block.BlockSnow;
import net.minecraft.world.IBlockAccess;

public class BlockSnowSheet extends BlockSnow {
    @Override
    public boolean shouldSideBeRendered(IBlockAccess block, int p_149646_2_, int p_149646_3_, int p_149646_4_, int p_149646_5_) {
        return super.shouldSideBeRendered(block, p_149646_2_, p_149646_3_, p_149646_4_, p_149646_5_);
    }
}
