package joshie.harvest.town.tracker;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import joshie.harvest.core.util.HFTracker;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.town.data.TownData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public abstract class TownTracker<T extends TownData> extends HFTracker {
    final Cache<BlockPos, T> closestCache = CacheBuilder.newBuilder().expireAfterWrite(1, TimeUnit.MINUTES).maximumSize(512).build();
    final HashMap<UUID, T> uuidMap = new HashMap<>();
    Set<T> townData = new HashSet<>();

    @Nullable
    private T getClosestTown(final BlockPos pos, boolean invalidateAll) {
        try {
            if (invalidateAll) closestCache.invalidateAll();
            return closestCache.get(pos, ()-> {
                T closest = null;
                double thatTownDistance = Double.MAX_VALUE;
                for (T town: townData) {
                    double thisTownDistance = town.getTownCentre().getDistance(pos.getX(), pos.getY(), pos.getZ());
                    if (closest == null || thisTownDistance < thatTownDistance) {
                        thatTownDistance = thisTownDistance;
                        closest = town;
                    }
                }

                return thatTownDistance > HFNPCs.TOWN_DISTANCE || closest == null ? null: closest;
            });
        } catch (Exception e) { return null; }
    }

    @Nullable
    public T getClosestTownToBlockPos(final BlockPos pos) {
        T data = getClosestTown(pos, false);
        return data == null ? getClosestTown(pos, true) : data;
    }

    public abstract T createNewTown(BlockPos blockPos, boolean builder);

    public T getTownByID(UUID townID) {
        T result = uuidMap.get(townID);
        return result == null ? null : result;
    }

    public BlockPos getCoordinatesForOverworldMine(Entity entity, int mineID) {
        return entity instanceof EntityPlayer ? ((EntityPlayer) entity).getBedLocation() : entity.worldObj.getSpawnPoint();
    }

    public int getMineIDFromCoordinates(BlockPos pos) {
        return -1;
    }

    public Rotation getMineOrientation(int mineID) {
        return Rotation.NONE;
    }
}
