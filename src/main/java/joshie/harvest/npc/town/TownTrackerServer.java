package joshie.harvest.npc.town;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import joshie.harvest.core.config.NPC;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.npc.entity.EntityNPCBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Callable;

public class TownTrackerServer extends TownTracker {
    private final Cache<Pair<Integer, BlockPos>, TownData> closestCache = CacheBuilder.newBuilder().build();
    private final Cache<Pair<Integer, BlockPos>, EntityNPCBuilder> closestBuilder = CacheBuilder.newBuilder().build();
    private final HashMap<UUID, TownData> uuidMap = new HashMap<>();
    private TIntObjectMap<Set<TownDataServer>> townData = new TIntObjectHashMap<>();

    private Set<TownDataServer> getTownsForDimension(int dimension, boolean dirty) {
        Set<TownDataServer> data = townData.get(dimension);
        if (data == null) {
            data = new HashSet<>();
            townData.put(dimension, data);
            if (dirty) {
                HFTrackers.markDirty();
            }
        }

        return data;
    }

    public void newDay(World world) {
        for (TownDataServer town: getTownsForDimension(world.provider.getDimension(), true)) {
            town.newDay(world);
        }
    }

    private EntityNPCBuilder createBuilder(TownData town, EntityLivingBase entity) {
        EntityNPCBuilder builder = new EntityNPCBuilder(entity.worldObj);
        builder.setPositionAndUpdate(entity.posX, entity.posY, entity.posZ);
        entity.worldObj.spawnEntityInWorld(entity);
        return builder;
    }

    @Override
    public TownData getTownByID(UUID townID) {
        TownData result = uuidMap.get(townID);
        return result == null ? NULL_TOWN : result;
    }

    @Override
    public EntityNPCBuilder getBuilderOrCreate(final TownData town, final EntityLivingBase player) {
        final int dimension = player.dimension;
        final BlockPos pos = new BlockPos(player);
        try {
            return closestBuilder.get(Pair.of(dimension, pos), new Callable<EntityNPCBuilder>() {
                @Override
                public EntityNPCBuilder call() throws Exception {
                    World world = player.worldObj;
                    EntityNPCBuilder builder = null;
                    double thatNPCDistance = Double.MAX_VALUE;
                    for (Entity entity: world.loadedEntityList) {
                        if (entity instanceof EntityNPCBuilder && ((EntityNPCBuilder)entity).getHomeTown() == town) {
                            double thisNPCDistance = town.getTownCentre().distanceSqToCenter(entity.posX, entity.posY, entity.posZ);
                            if (builder == null || thisNPCDistance < thatNPCDistance) {
                                builder = (EntityNPCBuilder) entity;
                            }
                        }
                    }

                    return builder == null ? createBuilder(town, player): builder;
                }
            });
        } catch (Exception e) { return createBuilder(town, player); }
    }

    @Override
    public TownData createNewTown(int dimension, BlockPos pos) {
        TownDataServer data = new TownDataServer(pos);
        getTownsForDimension(dimension, true).add(data);
        closestCache.invalidateAll(); //Reset the cache everytime we make a new town
        uuidMap.put(data.getID(), data);
        HFTrackers.markDirty();
        return data;
    }

    @Override
    public TownData getClosestTownToBlockPos(final int dimension, final BlockPos pos) {
        try {
            return closestCache.get(Pair.of(dimension, pos), new Callable<TownData>() {
                @Override
                public TownData call() throws Exception {
                    Set<TownDataServer> towns = getTownsForDimension(dimension, true);
                    TownData closest = null;
                    double thatTownDistance = Double.MAX_VALUE;
                    for (TownDataServer town: towns) {
                        double thisTownDistance = town.getTownCentre().getDistance(pos.getX(), pos.getY(), pos.getZ());
                        if (closest == null || thisTownDistance < thatTownDistance) {
                            thatTownDistance = thisTownDistance;
                            closest = town;
                        }
                    }

                    return thatTownDistance > NPC.townDistance || closest == null ? NULL_TOWN: closest;
                }
            });
        } catch (Exception e) { e.printStackTrace(); return NULL_TOWN; }


    }

    public void readFromNBT(NBTTagCompound nbt) {
        NBTTagList dimensions = nbt.getTagList("TownData", 10);
        for (int i = 0; i < dimensions.tagCount(); i++) {
            NBTTagCompound tag = dimensions.getCompoundTagAt(i);
            int dimension_id = tag.getInteger("DimensionID");
            Set<TownDataServer> data = getTownsForDimension(dimension_id, false);
            NBTTagList dimensionTowns = tag.getTagList("DimensionTowns", 10);
            for (int j = 0; j < dimensionTowns.tagCount(); j++) {
                NBTTagCompound townData = dimensionTowns.getCompoundTagAt(j);
                TownDataServer theData = new TownDataServer();
                theData.readFromNBT(townData);
                uuidMap.put(theData.getID(), theData);
                data.add(theData);
            }
        }
    }

    public void writeToNBT(NBTTagCompound nbt) {
        NBTTagList dimensions = new NBTTagList();
        for (int dimension: townData.keys()) {
            if (townData.get(dimension).size() > 0) {
                NBTTagCompound tag = new NBTTagCompound();
                NBTTagList town_list = new NBTTagList();
                tag.setInteger("DimensionID", dimension);
                for (TownDataServer data: townData.get(dimension)) {
                    NBTTagCompound townData = new NBTTagCompound();
                    data.writeToNBT(townData);
                    town_list.appendTag(townData);
                }

                tag.setTag("DimensionTowns", town_list);
                dimensions.appendTag(tag);
            }
        }

        nbt.setTag("TownData", dimensions);
    }
}
