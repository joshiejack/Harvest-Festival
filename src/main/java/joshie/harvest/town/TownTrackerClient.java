package joshie.harvest.town;

import net.minecraft.util.math.BlockPos;

import java.util.Set;

public class TownTrackerClient extends TownTracker {
    public void addTown(TownData data) {
        townData.add(data);
        uuidMap.put(data.getID(), data);
    }

    public void setTowns(Set<TownData> set) {
        townData = set;
        uuidMap.clear(); //Reset the map
        for (TownData data: set) {
            uuidMap.put(data.getID(), data);
        }
    }

    @Override
    public TownData createNewTown(BlockPos pos) {
        return new TownDataClient();
    }
}
