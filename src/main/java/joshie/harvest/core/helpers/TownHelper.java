package joshie.harvest.core.helpers;

import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.npc.entity.EntityNPCBuilder;
import joshie.harvest.town.TownData;
import joshie.harvest.town.TownTracker;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.UUID;

public class TownHelper {
    public static TownData getClosestTownToBlockPosOrCreate(World world, BlockPos pos) {
        TownData data = HFTrackers.getTownTracker(world).getClosestTownToBlockPos(pos);
        if (data == TownTracker.NULL_TOWN) {
            data = HFTrackers.getTownTracker(world).createNewTown(pos);
        }

        return data;
    }

    /** Called from shops **/
    public static TownData getClosestTownToPlayer(EntityLivingBase player) {
        return HFTrackers.getTownTracker(player.worldObj).getClosestTownToBlockPos(new BlockPos(player));
    }

    public static TownData getClosestTownToEntityOrCreate(EntityLiving entity) {
        TownData data = HFTrackers.getTownTracker(entity.worldObj).getClosestTownToBlockPos(new BlockPos(entity));
        if (data == TownTracker.NULL_TOWN) {
            data = HFTrackers.getTownTracker(entity.worldObj).createNewTown(new BlockPos(entity));
        }

        return data;
    }

    public static EntityNPCBuilder getClosestBuilderToEntityOrCreate(EntityLivingBase player) {
        return HFTrackers.getTownTracker(player.worldObj).getBuilderOrCreate(getClosestTownToPlayer(player), player);
    }

    public static TownData getTownByID(World world, UUID townID) {
        return HFTrackers.getTownTracker(world).getTownByID(townID);
    }
}
