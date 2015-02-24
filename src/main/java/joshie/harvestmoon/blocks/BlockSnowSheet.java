package joshie.harvestmoon.blocks;

import java.util.Random;

import joshie.harvestmoon.calendar.Season;
import joshie.harvestmoon.core.helpers.CalendarHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSnow;
import net.minecraft.entity.Entity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockSnowSheet extends BlockSnow {
    @Override
    public boolean shouldSideBeRendered(IBlockAccess block, int x, int y, int z, int side) {
        return super.shouldSideBeRendered(block, x, y, z, side);
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random rand) {
        if (!world.provider.canSnowAt(x, y, z, false)) {
            world.setBlockToAir(x, y, z);
        }

        super.updateTick(world, x, y, z, rand);
    }

    private void dieSnow(World world, int x, int y, int z) {
        if (!world.isRemote) {
            updateTick(world, x, y, z, world.rand);
        }
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
        super.onNeighborBlockChange(world, x, y, z, block);
        dieSnow(world, x, y, z);
    }

    @Override
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
        dieSnow(world, x, y, z);
    }

    @Override
    public void fillWithRain(World world, int x, int y, int z) {
        if (CalendarHelper.getSeason() == Season.WINTER) {
            int meta = world.getBlockMetadata(x, y, z);
            if (meta < 15) {
                world.setBlockMetadataWithNotify(x, y, z, meta + 1, 2);
            }
        }
    }
}
