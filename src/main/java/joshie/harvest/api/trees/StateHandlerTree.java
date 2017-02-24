package joshie.harvest.api.trees;

import joshie.harvest.api.crops.StateHandlerDefault;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import static net.minecraft.block.Block.FULL_BLOCK_AABB;

public class StateHandlerTree extends StateHandlerDefault<Tree> {
    private static final AxisAlignedBB SAPLING_AABB = new AxisAlignedBB(0.09999999403953552D, 0.0D, 0.09999999403953552D, 0.8999999761581421D, 0.800000011920929D, 0.8999999761581421D);
    private static final AxisAlignedBB JUVENILE_UPPER_AABB = new AxisAlignedBB(0.09999999403953552D, -1.0D, 0.09999999403953552D, 0.8999999761581421D, 0.800000011920929D, 0.8999999761581421D);
    private static final AxisAlignedBB JUVENILE_LOWER_AABB = new AxisAlignedBB(0.09999999403953552D, 0.0D, 0.09999999403953552D, 0.8999999761581421D, 1.800000011920929D, 0.8999999761581421D);
    private final IBlockState log;
    private final int stage1;
    private final int stage2;
    private final int stage3;

    public StateHandlerTree(IBlockState log, int stage1, int stage2, int stage3) {
        super(4);
        this.log = log;
        this.stage1 = stage1;
        this.stage2 = stage1 + stage2;
        this.stage3 = stage1 + stage2 + stage3;
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getState(IBlockAccess world, BlockPos pos, PlantSection section, Tree tree, int stage, boolean withered) {
        if (section == PlantSection.TOP) return stage < stage3 ? getState(4) : log;
        else if (stage < stage1) return getState(1);
        else if (stage < stage2) return getState(2);
        else if (stage < stage3) return getState(3);
        else return log;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockAccess world, BlockPos pos, PlantSection section, Tree tree, int stage, boolean withered) {
        if (section == PlantSection.TOP) return stage < stage3 ? JUVENILE_UPPER_AABB : FULL_BLOCK_AABB;
        else if (stage < stage1) return CROP_AABB;
        else if (stage < stage2) return SAPLING_AABB;
        else if (stage < stage3) return JUVENILE_LOWER_AABB;
        else return FULL_BLOCK_AABB;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockAccess world, BlockPos pos, PlantSection section, Tree tree, int stage, boolean withered) {
        if (section == PlantSection.TOP) return stage < stage3 ? JUVENILE_UPPER_AABB : FULL_BLOCK_AABB;
        else if (stage < stage1) return CROP_AABB;
        else if (stage < stage2) return SAPLING_AABB;
        else if (stage < stage3) return JUVENILE_LOWER_AABB;
        else return FULL_BLOCK_AABB;
    }
}