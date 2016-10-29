package joshie.harvest.town.tracker;

import joshie.harvest.town.data.TownDataClient;
import net.minecraft.util.math.BlockPos;

import java.util.Set;

public class TownTrackerClient extends TownTracker<TownDataClient> {
    public void addTown(TownDataClient data) {
        townData.add(data);
        uuidMap.put(data.getID(), data);
    }

    public void setTowns(Set<TownDataClient> set) {
        townData = set;
        uuidMap.clear(); //Reset the map
        for (TownDataClient data: set) {
            uuidMap.put(data.getID(), data);
        }
    }

    @Override
    public TownDataClient createNewTown(BlockPos pos) {
        return new TownDataClient();
    }
}
