package joshie.harvest.town;

import joshie.harvest.mining.HFMining;
import joshie.harvest.mining.MiningHelper;
import joshie.harvest.town.data.TownData;
import joshie.harvest.town.data.TownDataServer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

import static joshie.harvest.core.HFTrackers.getTowns;

public class TownHelper {
    private static BlockPos getDefaultCoordinates(@Nullable Entity entity) {
        return entity instanceof EntityPlayer ? ((EntityPlayer) entity).getBedLocation(0) : DimensionManager.getWorld(0).getSpawnPoint();
    }

    @SuppressWarnings("unchecked")
    public static <T extends TownData> T getClosestTownToBlockPos(World world, BlockPos pos) {
        //If we're in the mine, adjust the block position
        //Based on the mining id that we have been given
        if (world.provider.getDimension() == HFMining.MINING_ID) {
            return (T) getTowns(world).getClosestTownToBlockPos(getTowns(world).getCoordinatesForOverworldMine(null, MiningHelper.getMineID(pos)));
        } else if (world.provider.getDimension() != 0) {
            //If the world isn't the overworld, take the spawn coordinates instead
            return (T) getTowns(world).getClosestTownToBlockPos(getDefaultCoordinates(null));
        }

        return (T) getTowns(world).getClosestTownToBlockPos(pos);
    }

    @SuppressWarnings("unchecked")
    public static <T extends TownData> T getClosestTownToEntity(@Nonnull Entity entity) {
        BlockPos pos = new BlockPos(entity);
        //If we're in the mine, adjust the block position
        //Based on the mining id that we have been given
        if (entity.worldObj.provider.getDimension() == HFMining.MINING_ID) {
            return (T) getTowns(entity.worldObj).getClosestTownToBlockPos(getTowns(entity.worldObj).getCoordinatesForOverworldMine(null, MiningHelper.getMineID(pos)));
        } else if (entity.worldObj.provider.getDimension() != 0) {
            //If the world isn't the overworld, take the spawn coordinates instead
            return (T) getTowns(entity.worldObj).getClosestTownToBlockPos(getDefaultCoordinates(entity));
        }

        return (T) getTowns(entity.worldObj).getClosestTownToBlockPos(pos);
    }

    public static TownData getTownByID(World world, UUID townID) {
        return getTowns(world).getTownByID(townID);
    }

    public static void ensureBuilderExists(World world, BlockPos pos, TownData original) {
        if (!world.isRemote) {
            TownDataServer data = (TownDataServer) original;
            if (data.getBuilder((WorldServer) world) == null) {
                data.createNewBuilder(world, pos);
            }
        }
    }
}
