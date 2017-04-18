package joshie.harvest.town.tracker;

import com.google.common.collect.BiMap;
import joshie.harvest.town.data.TownDataClient;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collection;
import java.util.UUID;

@SideOnly(Side.CLIENT)
public class TownTrackerClient extends TownTracker<TownDataClient> {
    private static final TownDataClient NULL_TOWN = new TownDataClient() {
        @Override
        public boolean isNull() {
            return true;
        }
    };

    @Override
    public TownDataClient getNullTown() {
        return NULL_TOWN;
    }

    public void addTown(TownDataClient data, int mine) {
        townIDs.put(data.getID(), mine);
        uuidMap.put(data.getID(), data);
    }

    public void setTowns(Collection<TownDataClient> set, BiMap<UUID, Integer> ids) {
        townIDs = ids;
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
