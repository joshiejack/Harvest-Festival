package joshie.harvest.asm;

import joshie.harvest.api.calendar.Season;
import joshie.harvest.core.handlers.HFTrackers;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class BlockSnow extends net.minecraft.block.BlockSnow {
    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
        if (world.canBlockSeeSky(pos)) {
            if (!dieSnow(world, pos) && rand.nextFloat() < 0.1F) {
                if (HFTrackers.getCalendar(world).getTodaysWeather().isSnow()) {
                    int meta = state.getValue(LAYERS);
                    if (meta < 3) {
                        world.setBlockState(pos, state.withProperty(LAYERS, meta + 1), 2);
                    }
                }
            }
        } else super.updateTick(world, pos, state, rand);
    }

    private boolean dieSnow(World world, BlockPos pos) {
        if (!world.isRemote) {
            if (!world.provider.canSnowAt(pos, false)) {
                if (HFTrackers.getCalendar(world).getSeasonAtPos(world, pos) != Season.WINTER) return world.setBlockToAir(pos);
                else return false;
            } else return false;
        } else return true;
    }

    @Override
    public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entityIn) {
        dieSnow(world, pos);
    }
}