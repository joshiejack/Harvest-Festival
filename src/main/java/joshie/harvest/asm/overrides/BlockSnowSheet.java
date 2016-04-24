package joshie.harvest.asm.overrides;

import joshie.harvest.api.calendar.Season;
import joshie.harvest.core.handlers.HFTrackers;
import net.minecraft.block.BlockSnow;
import net.minecraft.entity.Entity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class BlockSnowSheet extends BlockSnow {
    @Override
    public boolean shouldSideBeRendered(IBlockAccess block, int x, int y, int z, int side) {
        return super.shouldSideBeRendered(block, x, y, z, side);
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random rand) {
        dieSnow(world, x, y, z);
    }

    private void dieSnow(World world, int x, int y, int z) {
        if (!world.isRemote) {
            if (!world.provider.canSnowAt(x, y, z, false)) {
                world.setBlockToAir(x, y, z);
            }
        }
    }

    @Override
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
        dieSnow(world, x, y, z);
    }

    @Override
    public void fillWithRain(World world, int x, int y, int z) {
        if (HFTrackers.getCalendar().getDate().getSeason() == Season.WINTER) {
            int meta = world.getBlockMetadata(x, y, z);
            if (meta < 15) {
                world.setBlockMetadataWithNotify(x, y, z, meta + 1, 2);
            }
        }
    }
}
