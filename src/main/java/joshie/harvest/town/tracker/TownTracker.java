package joshie.harvest.town.tracker;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import joshie.harvest.core.util.HFTracker;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.town.data.TownData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.DimensionManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.UUID;

public abstract class TownTracker<T extends TownData> extends HFTracker {
    final HashMap<UUID, T> uuidMap = new HashMap<>();
    BiMap<UUID, Integer> townIDs = HashBiMap.create();

    public abstract T getNullTown();

    @Nonnull
    private T getClosestTown(@Nonnull final BlockPos pos) {
        T closest = null;
        double thatTownDistance = Double.MAX_VALUE;
        for (T town: uuidMap.values()) {
            double thisTownDistance = town.getTownCentre().getDistance(pos.getX(), pos.getY(), pos.getZ());
            if (closest == null || thisTownDistance < thatTownDistance) {
                thatTownDistance = thisTownDistance;
                closest = town;
            }
        }

        return thatTownDistance > HFNPCs.TOWN_DISTANCE || closest == null ? getNullTown(): closest;
    }

    @Nonnull
    public T getClosestTownToBlockPos(@Nonnull BlockPos pos, boolean create) {
        T data = getClosestTown(pos);
        if (data == getNullTown() && create) {
            data = createNewTown(pos);
        }

        return data;
    }

    public abstract T createNewTown(BlockPos blockPos);

    public T getTownFromMineID(int mineID) {
        UUID uuid = townIDs.inverse().get(mineID);
        return uuid != null ? getTownByID(uuid) : getNullTown();
    }

    public T getTownByID(UUID townID) {
        T result = uuidMap.get(townID);
        return result == null ? getNullTown() : result;
    }

    public BlockPos getCoordinatesForOverworldMine(@Nullable Entity entity, int mineID) {
        return entity instanceof EntityPlayer ? ((EntityPlayer) entity).getBedLocation(0) : DimensionManager.getWorld(0).getSpawnPoint();
    }

    public int getMineIDFromCoordinates(BlockPos pos) {
        return -1;
    }

    public Rotation getMineOrientation(int mineID) {
        return Rotation.NONE;
    }
}
