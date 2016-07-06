package joshie.harvest.town;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import joshie.harvest.core.config.NPC;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.npc.entity.EntityNPCBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;

import java.util.*;
import java.util.concurrent.Callable;

public class TownTrackerServer extends TownTracker {
    private final Cache<BlockPos, TownData> closestCache = CacheBuilder.newBuilder().build();
    private final Cache<BlockPos, EntityNPCBuilder> closestBuilder = CacheBuilder.newBuilder().build();
    private final HashMap<UUID, TownData> uuidMap = new HashMap<>();
    private Set<TownDataServer> townData = new HashSet<>();
    private Set<EntityNPCBuilder> builders = new HashSet<>();

    public void newDay() {
        for (TownDataServer town: townData) {
            town.newDay(getWorld());
        }
    }

    @Override
    public void addBuilder(EntityNPCBuilder npc) {
        builders.add(npc);
        //Update the list, Removing any that are actually dead
        Iterator<EntityNPCBuilder> it = builders.iterator();
        while (it.hasNext()) {
            if (!it.next().isEntityAlive()) {
                it.remove();
            }
        }
    }

    private EntityNPCBuilder createBuilder(EntityLivingBase entity) {
        EntityNPCBuilder builder = new EntityNPCBuilder(entity.worldObj);
        builder.setPositionAndUpdate(entity.posX, entity.posY, entity.posZ);
        builder.resetSpawnHome();
        entity.worldObj.spawnEntityInWorld(builder);
        return builder;
    }

    @Override
    public TownData getTownByID(UUID townID) {
        TownData result = uuidMap.get(townID);
        return result == null ? NULL_TOWN : result;
    }

    @Override
    public EntityNPCBuilder getBuilderOrCreate(final TownData town, final EntityLivingBase player) {
        final BlockPos pos = new BlockPos(player);
        try {
            return closestBuilder.get(pos, new Callable<EntityNPCBuilder>() {
                @Override
                public EntityNPCBuilder call() throws Exception {
                    EntityNPCBuilder builder = null;
                    double thatNPCDistance = Double.MAX_VALUE;
                    //Attempt to grab the loaded entity first
                    for (Entity entity: builders) {
                        if (entity instanceof EntityNPCBuilder && ((EntityNPCBuilder)entity).getHomeTown() == town) {
                            double thisNPCDistance = town.getTownCentre().distanceSqToCenter(entity.posX, entity.posY, entity.posZ);
                            if (builder == null || thisNPCDistance < thatNPCDistance) {
                                builder = (EntityNPCBuilder) entity;
                                thatNPCDistance = thisNPCDistance;
                            }
                        }
                    }

                    return builder == null ? createBuilder(player): builder;
                }
            });
        } catch (Exception e) { return createBuilder(player); }
    }

    @Override
    public TownData createNewTown(BlockPos pos) {
        TownDataServer data = new TownDataServer(pos);
        townData.add(data);
        closestCache.invalidateAll(); //Reset the cache everytime we make a new town
        uuidMap.put(data.getID(), data);
        HFTrackers.markDirty(getDimension());
        return data;
    }

    @Override
    public TownData getClosestTownToBlockPos(final BlockPos pos) {
        try {
            return closestCache.get(pos, new Callable<TownData>() {
                @Override
                public TownData call() throws Exception {
                    TownData closest = null;
                    double thatTownDistance = Double.MAX_VALUE;
                    for (TownDataServer town: townData) {
                        double thisTownDistance = town.getTownCentre().getDistance(pos.getX(), pos.getY(), pos.getZ());
                        if (closest == null || thisTownDistance < thatTownDistance) {
                            thatTownDistance = thisTownDistance;
                            closest = town;
                        }
                    }

                    return thatTownDistance > NPC.TOWN_DISTANCE || closest == null ? NULL_TOWN: closest;
                }
            });
        } catch (Exception e) { e.printStackTrace(); return NULL_TOWN; }
    }

    public void readFromNBT(NBTTagCompound nbt) {
        townData = new HashSet<>();
        NBTTagList dimensionTowns = nbt.getTagList("Towns", 10);
        for (int j = 0; j < dimensionTowns.tagCount(); j++) {
            NBTTagCompound tag = dimensionTowns.getCompoundTagAt(j);
            TownDataServer theData = new TownDataServer();
            theData.readFromNBT(tag);
            uuidMap.put(theData.getID(), theData);
            townData.add(theData);
        }
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        NBTTagList town_list = new NBTTagList();
        for (TownDataServer data: townData) {
            NBTTagCompound townData = new NBTTagCompound();
            data.writeToNBT(townData);
            town_list.appendTag(townData);
        }

        nbt.setTag("Towns", town_list);
        return nbt;
    }
}
