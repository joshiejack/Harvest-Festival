package joshie.harvest.crops.block;

import joshie.harvest.core.base.block.BlockHFLeaves;
import joshie.harvest.crops.block.BlockLeavesFruit.LeavesFruit;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import javax.annotation.Nonnull;
import java.util.Locale;

public class BlockLeavesFruit extends BlockHFLeaves<BlockLeavesFruit, LeavesFruit> {
    @SuppressWarnings("ConstantConditions")
    public BlockLeavesFruit() {
        super(LeavesFruit.class);
        setCreativeTab(null);
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    public IBlockState getActualState(@Nonnull IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        //Season season = HFApi.calendar.getSeasonProvider(CalendarHelper.getSeason())
        return state;
    }

    public enum LeavesFruit implements IStringSerializable {
        APPLE, GRAPE, ORANGE, PEACH;

        @Override
        public String getName() {
            return toString().toLowerCase(Locale.ENGLISH);
        }
    }
}
