package joshie.harvest.town;

import net.minecraft.util.math.BlockPos;

public class TownTrackerClient extends TownTracker {
    @Override
    public TownData createNewTown(BlockPos pos) {
        return new TownDataClient();
    }
}
