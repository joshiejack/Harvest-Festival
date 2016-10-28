package joshie.harvest.town.tracker;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.network.PacketHandler;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.npc.entity.EntityNPCBuilder;
import joshie.harvest.town.data.TownData;
import joshie.harvest.town.data.TownDataServer;
import joshie.harvest.town.packet.PacketNewTown;
import joshie.harvest.town.packet.PacketSyncTowns;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.Map.Entry;
import java.util.UUID;

import static joshie.harvest.town.BuildingLocations.MINEENTRANCE;

public class TownTrackerServer extends TownTracker<TownDataServer> {
    private BiMap<UUID, Integer> townIDs = HashBiMap.create();

    public void newDay() {
        for (TownDataServer town: townData) {
            town.newDay(getWorld());
        }
    }

    public void syncToPlayer(EntityPlayer player) {
        PacketHandler.sendToClient(new PacketSyncTowns(townData), player);
        for (TownDataServer town: townData) {
            town.getQuests().syncAllQuests();
        }
    }

    @Override
    public BlockPos getCoordinatesForOverworldMine(Entity entity, int mineID) {
        BlockPos default_ = super.getCoordinatesForOverworldMine(entity, mineID);
        UUID uuid = townIDs.inverse().get(mineID);
        if (uuid == null) return default_;
        TownData data = uuidMap.get(uuid);
        if (data == null) return default_;
        if (!data.hasBuilding(MINEENTRANCE.getResource())) return data.getTownCentre();
        BlockPos location = data.getCoordinatesFor(MINEENTRANCE);
        return location != null? location : data.getTownCentre();
    }

    @Override
    public Rotation getMineOrientation(int mineID) {
        UUID uuid = townIDs.inverse().get(mineID);
        if (uuid == null) return Rotation.NONE;
        TownDataServer data = uuidMap.get(uuid);
        if (data == null || !data.hasBuilding(MINEENTRANCE.getResource())) return Rotation.NONE;
        return data.getFacingFor(MINEENTRANCE.getResource());
    }

    @Override
    public int getMineIDFromCoordinates(BlockPos pos) {
        TownData data = getClosestTownToBlockPos(pos);
        if (data == null) return -1;
        if (!data.hasBuilding(MINEENTRANCE.getResource())) return -1;
        if (townIDs.containsKey(data.getID())) {
            return townIDs.get(data.getID());
        } else return matchUUIDWithMineID(data.getID());
    }

    private int matchUUIDWithMineID(UUID uuid) {
        for (int i = 0; i < 32000; i++) { //Add a mineid to uuid entry
            if (!townIDs.inverse().containsKey(i)) {
                townIDs.put(uuid, i);
                HFTrackers.markDirty(getDimension());
                return i;
            }
        }

        return 0;
    }

    public void createNewBuilder(BlockPos pos, TownDataServer data) {
        if (!data.isDead(HFNPCs.BUILDER)) {
            World world = getWorld();
            EntityNPCBuilder creator = new EntityNPCBuilder(world);
            creator.setPositionAndUpdate(pos.getX(), pos.getY() + 1.5D, pos.getZ());
            creator.setSpawnHome(data); //Set the spawn town
            creator.setUniqueId(data.getID()); //Marking the builder as having the same data
            world.spawnEntityInWorld(creator); //Towns owner now spawned
        }
    }

    @Override
    public TownDataServer createNewTown(BlockPos pos, boolean builder) {
        World world = getWorld();
        TownDataServer data = new TownDataServer(getDimension(), pos);
        if (builder) {
            EntityNPCBuilder creator = new EntityNPCBuilder(world);
            creator.setSpawnHome(data); //Set the spawn town
            creator.setUniqueId(data.getID()); //Marking the builder as having the same data
            creator.setPositionAndUpdate(pos.getX(), pos.getY() + 1.5D, pos.getZ());
            world.spawnEntityInWorld(creator); //Towns owner now spawned
        }

        townData.add(data);
        closestCache.invalidateAll(); //Reset the cache everytime we make a new town
        uuidMap.put(data.getID(), data);
        matchUUIDWithMineID(data.getID());
        PacketHandler.sendToDimension(getDimension(), new PacketNewTown(data)); //Sync to everyone on this dimension
        HFTrackers.markDirty(getDimension());
        return data;
    }

    public void readFromNBT(NBTTagCompound nbt) {
        townData = new HashSet<>(); //Reset the data
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
