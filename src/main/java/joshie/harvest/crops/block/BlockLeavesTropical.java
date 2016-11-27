package joshie.harvest.crops.block;

import joshie.harvest.core.base.block.BlockHFLeaves;
import joshie.harvest.crops.block.BlockLeavesTropical.LeavesTropical;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import javax.annotation.Nonnull;
import java.util.Locale;

public class BlockLeavesTropical extends BlockHFLeaves<BlockLeavesTropical, LeavesTropical> {
    @SuppressWarnings("ConstantConditions")
    public BlockLeavesTropical() {
        super(LeavesTropical.class);
        setCreativeTab(null);
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    public IBlockState getActualState(@Nonnull IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return state;
    }

    public enum LeavesTropical implements IStringSerializable {
        BANANA;

        @Override
        public String getName() {
            return toString().toLowerCase(Locale.ENGLISH);
        }
    }
}
