package joshie.harvest.quests.block;

import joshie.harvest.api.ticking.DailyTickableBlock;
import joshie.harvest.api.ticking.DailyTickableBlock.Phases;
import joshie.harvest.core.base.tile.TileHarvest;
import joshie.harvest.quests.HFQuests;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileQuestBoard extends TileHarvest {
    private static final DailyTickableBlock TICKABLE = new DailyTickableBlock(Phases.POST) {
        @Override
        public boolean isStateCorrect(World world, BlockPos pos, IBlockState state) {
            return state.getBlock() == HFQuests.QUEST_BLOCK;
        }

        @Override
        @SuppressWarnings("ConstantConditions")
        public void newDay(World world, BlockPos pos, IBlockState state) {
            ((TileQuestBoard)world.getTileEntity(pos)).saveAndRefresh();
        }
    };

    @Override
    public DailyTickableBlock getTickableForTile() {
        return TICKABLE;
    }
}
