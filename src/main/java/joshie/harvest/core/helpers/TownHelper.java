package joshie.harvest.core.helpers;

import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.town.TownData;
import joshie.harvest.town.TownDataServer;
import joshie.harvest.town.TownTracker;
import joshie.harvest.town.TownTrackerServer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import java.util.UUID;

import static joshie.harvest.core.handlers.HFTrackers.getTownTracker;

public class TownHelper {
    public static <T extends TownData> T getClosestTownToBlockPos(World world, BlockPos pos) {
        return (T) getTownTracker(world).getClosestTownToBlockPos(pos);
    }

    /** Called from shops **/
    public static <T extends TownData> T getClosestTownToEntity(EntityLivingBase entity) {
        return (T) getTownTracker(entity.worldObj).getClosestTownToBlockPos(new BlockPos(entity));
    }

    public static TownData getTownByID(World world, UUID townID) {
        return getTownTracker(world).getTownByID(townID);
    }

    public static void ensureTownExists(World world, BlockPos pos) {
        TownTrackerServer tracker = HFTrackers.getTownTracker(world);
        TownData data = tracker.getClosestTownToBlockPos(pos);
        if (data == null || data == TownTracker.NULL_TOWN) {
            data = tracker.createNewTown(pos);
        }

        //Create a builder if one doesn't exist
        if (data instanceof TownDataServer) {
            TownDataServer server = (TownDataServer) data;
            if(server.getBuilder((WorldServer) world) == null) {
                tracker.createNewBuilder(pos, server);
            }
        }
    }
}
