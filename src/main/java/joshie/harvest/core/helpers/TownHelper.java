package joshie.harvest.core.helpers;

import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.npc.entity.EntityNPCBuilder;
import joshie.harvest.npc.town.TownTracker;
import joshie.harvest.npc.town.TownData;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;

import java.util.UUID;

public class TownHelper {
    public static TownData getClosestTownToBlockPosOrCreate(int dimension, BlockPos pos) {
        TownData data = HFTrackers.getTownTracker().getClosestTownToBlockPos(dimension, pos);
        if (data == TownTracker.NULL_TOWN) {
            data = HFTrackers.getTownTracker().createNewTown(dimension, pos);
        }

        return data;
    }

    /** Called from shops **/
    public static TownData getClosestTownToPlayer(EntityLivingBase player) {
        return HFTrackers.getTownTracker().getClosestTownToBlockPos(player.dimension, new BlockPos(player));
    }

    public static TownData getClosestTownToEntityOrCreate(EntityLiving entity) {
        TownData data = HFTrackers.getTownTracker().getClosestTownToBlockPos(entity.dimension, new BlockPos(entity));
        if (data == TownTracker.NULL_TOWN) {
            data = HFTrackers.getTownTracker().createNewTown(entity.dimension, new BlockPos(entity));
        }

        return data;
    }

    public static EntityNPCBuilder getClosestBuilderToEntityOrCreate(EntityLivingBase player) {
        return HFTrackers.getTownTracker().getBuilderOrCreate(getClosestTownToPlayer(player), player);
    }

    public static TownData getTownByID(UUID townID) {
        return HFTrackers.getTownTracker().getTownByID(townID);
    }
}
