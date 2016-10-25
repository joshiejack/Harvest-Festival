package joshie.harvest.crops.handlers.state;

import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.crops.Crop;
import joshie.harvest.api.crops.StateHandlerBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStem;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import javax.annotation.Nullable;

public abstract class StateHandlerStem extends StateHandlerBlock {
    public StateHandlerStem(Block block) {
        super(block);
    }

    @Override
    public int getColor(IBlockAccess world, BlockPos pos, IBlockState renderState, @Nullable Season season, Crop crop, boolean withered) {
        if (withered) return 0x753A00;
        else {
            int i = renderState.getValue(BlockStem.AGE);
            int j = i * 32;
            int k = 255 - i * 8;
            int l = i * 4;
            return j << 16 | k << 8 | l;
        }
    }
}