package joshie.harvest.town;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import joshie.harvest.api.buildings.BuildingLocation;
import joshie.harvest.buildings.HFBuildings;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.network.PacketHandler;
import joshie.harvest.core.util.Direction;
import joshie.harvest.npc.entity.EntityNPCBuilder;
import joshie.harvest.town.packets.PacketNewTown;
import joshie.harvest.town.packets.PacketSyncTowns;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.Callable;

import static joshie.harvest.core.util.Direction.MN_R0;
import static joshie.harvest.town.TownData.MINE_ENTRANCE;

public class TownTrackerServer extends TownTracker {
    private static final BuildingLocation MINE = new BuildingLocation(HFBuildings.MINING_HILL, MINE_ENTRANCE);
    private final Cache<BlockPos, EntityNPCBuilder> closestBuilder = CacheBuilder.newBuilder().build();
    private BiMap<UUID, Integer> townIDs = HashBiMap.create();
    private Set<EntityNPCBuilder> builders = new HashSet<>();

    public void newDay() {
        for (TownData town: townData) {
            town.newDay(getWorld());
        }
    }

    public void syncToPlayer(EntityPlayer player) {
        PacketHandler.sendToClient(new PacketSyncTowns(townData), player);
    }

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

    @Override
    public BlockPos getCoordinatesForOverworldMine(Entity entity, int mineID) {
        BlockPos default_ = super.getCoordinatesForOverworldMine(entity, mineID);
        UUID uuid = townIDs.inverse().get(mineID);
        if (uuid == null) return default_;
        TownData data = uuidMap.get(uuid);
        if (data == null || !data.hasBuilding(MINE.getResource())) return default_;
        return data.getCoordinatesFor(MINE);
    }

    @Override
    public Direction getMineOrientation(int mineID) {
        UUID uuid = townIDs.inverse().get(mineID);
        if (uuid == null) return MN_R0;
        TownData data = uuidMap.get(uuid);
        if (data == null || !data.hasBuilding(MINE.getResource())) return Direction.MN_R0;
        return data.getFacingFor(MINE.getResource());
    }

    @Override
    public int getMineIDFromCoordinates(BlockPos pos) {
        TownData data = getClosestTownToBlockPos(pos);
        if (data == null) return -1;
        if (!data.hasBuilding(MINE.getResource())) return -1;
        if (townIDs.containsKey(data.getID())) {
            return townIDs.get(data.getID());
        } else return matchUUIDWithMineID(data.getID());
    }

    private int matchUUIDWithMineID(UUID uuid) {
        for (int i = 0; i < 32000; i++) { //Add a mineid to uuid entry
            if (townIDs.inverse().containsKey(i)) continue;
            else {
                townIDs.put(uuid, i);
                HFTrackers.markDirty(getDimension());
                return i;
            }
        }

        return 0;
    }

    private EntityNPCBuilder createBuilder(EntityLivingBase entity) {
        EntityNPCBuilder builder = new EntityNPCBuilder(entity.worldObj);
        builder.setPositionAndUpdate(entity.posX, entity.posY, entity.posZ);
        builder.resetSpawnHome();
        entity.worldObj.spawnEntityInWorld(builder);
        return builder;
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
        matchUUIDWithMineID(data.getID());
        PacketHandler.sendToDimension(getDimension(), new PacketNewTown(data)); //Sync to everyone on this dimension
        HFTrackers.markDirty(getDimension());
        return data;
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

        townIDs = HashBiMap.create();
        NBTTagList ids = nbt.getTagList("IDs", 10);
        for (int j = 0; j < ids.tagCount(); j++) {
            NBTTagCompound tag = ids.getCompoundTagAt(j);
            int id = tag.getInteger("ID");
            UUID uuid = UUID.fromString(tag.getString("UUID"));
            townIDs.put(uuid, id);
        }
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        NBTTagList town_list = new NBTTagList();
        for (TownData data: townData) {
            NBTTagCompound townData = new NBTTagCompound();
            data.writeToNBT(townData);
            town_list.appendTag(townData);
        }

        nbt.setTag("Towns", town_list);

        //Ids
        NBTTagList ids = new NBTTagList();
        for (Entry<UUID, Integer> entry: townIDs.entrySet()) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setInteger("ID", entry.getValue());
            tag.setString("UUID", entry.getKey().toString());
            ids.appendTag(tag);
        }

        nbt.setTag("IDs", ids);
        return nbt;
    }
}
