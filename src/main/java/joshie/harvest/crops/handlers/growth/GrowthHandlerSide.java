package joshie.harvest.crops.handlers.growth;

import joshie.harvest.api.crops.Crop;
import joshie.harvest.api.crops.GrowthHandler;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GrowthHandlerSide extends GrowthHandler<Crop> {
    protected final Block block;

    public GrowthHandlerSide(Block block) {
        this.block = block;
    }

    @Override
    public boolean canPlantSeedAt(World world, BlockPos pos, IBlockState soil, Crop crop, BlockPos original) {
        return (crop.getRegrowStage() > 0 && !pos.equals(original)) || (crop.getRegrowStage() == 0 && super.canPlantSeedAt(world, pos, soil, crop, original));
    }

    @Override
    public int grow(World world, BlockPos pos, Crop crop, int stage) {
        int newStage = super.grow(world, pos, crop, stage);
        if (newStage == crop.getStages()) {
            if (crop.getRegrowStage() > 0 && attemptToGrowToSide(world, pos)) {
                return crop.getRegrowStage();
            } else world.setBlockState(pos, getBlockState(world));
        }

        return newStage;
    }

    protected IBlockState getBlockState(World world) {
        return block.getDefaultState();
    }

    @Override
    public boolean canHarvest(Crop crop, int stage) {
        return false; //Always return false, as we never grab the crop from the plant itself
    }

    @SuppressWarnings("deprecation")
    private boolean attemptToGrowToSide(World world, BlockPos pos) {
        if (world.isAirBlock(pos.add(1, 0, 0))) { //If it's air, then let's grow some shit
            return world.setBlockState(pos.add(1, 0, 0), block.getStateFromMeta(0), 2); //0 = x-
        } else if (world.isAirBlock(pos.add(0, 0, -1))) {
            return world.setBlockState(pos.add(0, 0, -1), block.getStateFromMeta(1), 2); //1 = z+
        } else if (world.isAirBlock(pos.add(0, 0, 1))) {
            return world.setBlockState(pos.add(0, 0, 1), block.getStateFromMeta(2), 2); //2 = z-
        } else if (world.isAirBlock(pos.add(-1, 0, 0))) {
            return world.setBlockState(pos.add(-1, 0, 0), block.getStateFromMeta(2), 2); //3 = x-
        }

        return false;
    }
}