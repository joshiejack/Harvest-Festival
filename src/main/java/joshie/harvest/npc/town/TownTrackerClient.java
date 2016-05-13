package joshie.harvest.npc.town;

import net.minecraft.util.math.BlockPos;

public class TownTrackerClient extends TownTracker {
    @Override
    public TownData createNewTown(int dimension, BlockPos pos) {
        return new TownDataClient();
    }
}
