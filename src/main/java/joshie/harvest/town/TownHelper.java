package joshie.harvest.town;

import joshie.harvest.core.handlers.HFTrackers;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import java.util.UUID;

import static joshie.harvest.core.handlers.HFTrackers.getTownTracker;

public class TownHelper {
    @SuppressWarnings("unchecked")
    public static <T extends TownData> T getClosestTownToBlockPos(World world, BlockPos pos) {
        return (T) getTownTracker(world).getClosestTownToBlockPos(pos);
    }

    /** Called from shops **/
    @SuppressWarnings("unchecked")
    public static <T extends TownData> T getClosestTownToEntity(EntityLivingBase entity) {
        return (T) getTownTracker(entity.worldObj).getClosestTownToBlockPos(new BlockPos(entity));
    }

    public static TownData getTownByID(World world, UUID townID) {
        return getTownTracker(world).getTownByID(townID);
    }

    public static boolean ensureTownExists(World world, BlockPos pos) {
        boolean ret = true;
        TownTrackerServer tracker = HFTrackers.getTownTracker(world);
        TownData data = tracker.getClosestTownToBlockPos(pos);
        if (data == null || data == TownTracker.NULL_TOWN) {
            data = tracker.createNewTown(pos, true);
            ret = false; //Returning false because we spawned a builder
        }

        //Create a builder if one doesn't exist
        if (data instanceof TownDataServer) {
            TownDataServer server = (TownDataServer) data;
            if(server.getBuilder((WorldServer) world) == null) {
                tracker.createNewBuilder(pos, server);
                return false; //Returning false because we spawned a builder
            }
        }

        return ret;
    }

    public static boolean createTownIfDoesntExist(World world, BlockPos pos) {
        TownTrackerServer tracker = HFTrackers.getTownTracker(world);
        TownData data = tracker.getClosestTownToBlockPos(pos);
        if (data == null || data == TownTracker.NULL_TOWN) {
            tracker.createNewTown(pos, false);
            return false;
        } else return true;
    }
}
