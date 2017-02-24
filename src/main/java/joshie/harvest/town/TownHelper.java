package joshie.harvest.town;

import joshie.harvest.api.town.ITownHelper;
import joshie.harvest.api.town.Town;
import joshie.harvest.core.util.annotations.HFApiImplementation;
import joshie.harvest.mining.HFMining;
import joshie.harvest.mining.MiningHelper;
import joshie.harvest.town.data.TownData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

import static joshie.harvest.core.HFTrackers.getTowns;

@HFApiImplementation
public class TownHelper implements ITownHelper {
    public static final TownHelper INSTANCE = new TownHelper();

    private static BlockPos getDefaultCoordinates(@Nullable Entity entity) {
        return entity instanceof EntityPlayer ? ((EntityPlayer) entity).getBedLocation(0) : DimensionManager.getWorld(0).getSpawnPoint();
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    private static <T extends TownData> T getClosestTownToBlockPos(@Nonnull World world, @Nonnull BlockPos pos, @Nullable Entity entity, boolean create) {
        //If we're in the mine, adjust the block position
        //Based on the mining id that we have been given
        if (world.provider.getDimension() == HFMining.MINING_ID) {
            return (T) getTowns(world).getClosestTownToBlockPos(getTowns(world).getCoordinatesForOverworldMine(null, MiningHelper.getMineID(pos)), create);
        } else if (world.provider.getDimension() != 0) {
            //If the world isn't the overworld, take the spawn coordinates instead
            return (T) getTowns(world).getClosestTownToBlockPos(getDefaultCoordinates(entity), create);
        }

        return (T) getTowns(world).getClosestTownToBlockPos(pos, create);
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    public static <T extends TownData> T getClosestTownToBlockPos(World world, BlockPos pos, boolean create) {
        return getClosestTownToBlockPos(world, pos, null, create);
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    public static <T extends TownData> T getClosestTownToEntity(@Nonnull Entity entity, boolean create) {
        return getClosestTownToBlockPos(entity.worldObj, new BlockPos(entity), entity, create);
    }

    public static TownData getTownByID(World world, UUID townID) {
        return getTowns(world).getTownByID(townID);
    }

    @Override
    public Town getTownForBlockPos(World world, BlockPos pos) {
        return getClosestTownToBlockPos(world, pos, false);
    }

    @Override
    public Town getTownForEntity(Entity entity) {
        return getClosestTownToEntity(entity, false);
    }
}
