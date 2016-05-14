package joshie.harvest.npc.town;

import joshie.harvest.core.helpers.NBTHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.UUID;

public class TownDataServer extends TownData {
    public GatheringData gathering = new GatheringData();

    public TownDataServer() {}
    public TownDataServer(BlockPos pos) {
        townCentre = pos;
        uuid = UUID.randomUUID();
    }

    public void newDay(World world) {
        gathering.newDay(world, buildings.values());
    }
    
    public void readFromNBT(NBTTagCompound nbt) {
        uuid = NBTHelper.readUUID("Town", nbt);
        townCentre = NBTHelper.readBlockPos("TownCentre", nbt);
        gathering.readFromNBT(nbt);
        NBTTagList list = nbt.getTagList("TownBuildingList", 10);
        for (int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound tag = list.getCompoundTagAt(i);
            TownBuilding building = new TownBuilding();
            building.readFromNBT(tag);
            buildings.put(building.building.getResource(), building);
        }
    }

    public void writeToNBT(NBTTagCompound nbt) {
        NBTHelper.writeBlockPos("TownCentre", nbt, townCentre);
        NBTHelper.writeUUID("Town", nbt, uuid);
        gathering.writeToNBT(nbt);
        NBTTagList list = new NBTTagList();
        for (ResourceLocation name : buildings.keySet()) {
            NBTTagCompound tag = new NBTTagCompound();
            buildings.get(name).writeToNBT(tag);
            list.appendTag(tag);
        }

        nbt.setTag("TownBuildingList", list);
    }
}
