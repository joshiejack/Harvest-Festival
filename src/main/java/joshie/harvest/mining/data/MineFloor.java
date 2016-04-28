package joshie.harvest.mining.data;

import joshie.harvest.blocks.HFBlocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static joshie.harvest.core.helpers.generic.MCServerHelper.getWorld;

public class MineFloor extends MineBlock {
    public MineFloor() {
    }

    public MineFloor(int dim, BlockPos pos) {
        super(dim, pos);
    }

    @Override
    public String getType() {
        return "floor";
    }

    @Override
    public void newDay(int level) {
        World world = getWorld(dimension);
        int meta = world.rand.nextInt(13) <= 10 ? 0 : world.rand.nextInt(16);
        world.setBlockState(position, HFBlocks.DIRT.getStateFromMeta(meta), 2);
    }
}