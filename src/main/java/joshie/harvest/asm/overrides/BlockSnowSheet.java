package joshie.harvest.asm.overrides;

import joshie.harvest.api.calendar.Season;
import joshie.harvest.core.handlers.HFTrackers;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class BlockSnowSheet extends BlockSnow {
    @Override
    public boolean shouldSideBeRendered(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        return super.shouldSideBeRendered(state, world, pos, side);
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
        dieSnow(world, pos);
    }

    private void dieSnow(World world, BlockPos pos) {
        if (!world.isRemote) {
            if (!world.provider.canSnowAt(pos, false)) {
                world.setBlockToAir(pos);
            }
        }
    }

    @Override
    public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entityIn) {
        dieSnow(world, pos);
    }

    @Override
    public void fillWithRain(World world, BlockPos pos) {
        if (HFTrackers.getCalendar().getDate().getSeason() == Season.WINTER) {
            IBlockState state = world.getBlockState(pos);
            int meta = state.getValue(LAYERS);
            if (meta < 15) {
                world.setBlockState(pos, state.withProperty(LAYERS, meta + 1), 2);
            }
        }
    }
}