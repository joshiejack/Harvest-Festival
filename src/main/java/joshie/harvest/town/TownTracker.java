package joshie.harvest.town;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import joshie.harvest.core.HFTracker;
import joshie.harvest.core.util.Direction;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.npc.entity.EntityNPCBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Callable;

public abstract class TownTracker extends HFTracker {
    public static final TownData NULL_TOWN = new TownData().setUUID(UUID.fromString("5b529b64-62dc-35df-416c-05e0210f6ab0"));
    protected final Cache<BlockPos, TownData> closestCache = CacheBuilder.newBuilder().build();
    protected final HashMap<UUID, TownData> uuidMap = new HashMap<>();
    protected Set<TownData> townData = new HashSet<>();

    public TownData getClosestTownToBlockPos(final BlockPos pos) {
        try {
            return closestCache.get(pos, new Callable<TownData>() {
                @Override
                public TownData call() throws Exception {
                    TownData closest = null;
                    double thatTownDistance = Double.MAX_VALUE;
                    for (TownData town: townData) {
                        double thisTownDistance = town.getTownCentre().getDistance(pos.getX(), pos.getY(), pos.getZ());
                        if (closest == null || thisTownDistance < thatTownDistance) {
                            thatTownDistance = thisTownDistance;
                            closest = town;
                        }
                    }

                    return thatTownDistance > HFNPCs.TOWN_DISTANCE || closest == null ? NULL_TOWN: closest;
                }
            });
        } catch (Exception e) { return NULL_TOWN; }
    }

    public abstract TownData createNewTown(BlockPos blockPos);

    public EntityNPCBuilder getBuilderOrCreate(TownData town, EntityLivingBase player) {
        return new EntityNPCBuilder(player.worldObj);
    }

    public TownData getTownByID(UUID townID) {
        TownData result = uuidMap.get(townID);
        return result == null ? NULL_TOWN : result;
    }

    public BlockPos getCoordinatesForOverworldMine(Entity entity, int mineID) {
        return entity instanceof EntityPlayer ? ((EntityPlayer) entity).getBedLocation() : entity.worldObj.getSpawnPoint();
    }

    public int getMineIDFromCoordinates(BlockPos pos) {
        return -1;
    }

    public Direction getMineOrientation(int mineID) {
        return Direction.MN_R0;
    }
}
