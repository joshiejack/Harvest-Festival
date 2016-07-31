package joshie.harvest.mining.blocks;

import joshie.harvest.core.HFTab;
import joshie.harvest.core.base.BlockHFEnumRotatableMeta;
import joshie.harvest.mining.blocks.BlockLadder.Ladder;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockLadder extends BlockHFEnumRotatableMeta<BlockLadder, Ladder> {
    protected static final AxisAlignedBB LADDER_EAST_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.1875D, 1.0D, 1.0D);
    protected static final AxisAlignedBB LADDER_WEST_AABB = new AxisAlignedBB(0.8125D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB LADDER_SOUTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.1875D);
    protected static final AxisAlignedBB LADDER_NORTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.8125D, 1.0D, 1.0D, 1.0D);

    public enum Ladder implements IStringSerializable {
        WOOD;

        @Override
        public String getName() {
            return toString().toLowerCase();
        }
    }

    public BlockLadder() {
        super(Material.ROCK, Ladder.class, HFTab.MINING);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        switch (state.getValue(FACING)) {
            case NORTH:
                return LADDER_NORTH_AABB;
            case SOUTH:
                return LADDER_SOUTH_AABB;
            case WEST:
                return LADDER_WEST_AABB;
            case EAST:
            default:
                return LADDER_EAST_AABB;
        }
    }

    @Override
    public boolean isLadder(IBlockState state, IBlockAccess world, BlockPos pos, EntityLivingBase entity) {
        return true;
    }
}
