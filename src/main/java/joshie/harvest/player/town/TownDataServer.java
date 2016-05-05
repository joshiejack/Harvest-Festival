package joshie.harvest.player.town;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;

public class TownDataServer extends TownData {
    public GatheringData gathering = new GatheringData();
    
    //TODO: SYNC THE TOWN DATA TO THE CLIENT
    public void sync (EntityPlayerMP player) {
        
    }

    public void newDay() {
        gathering.newDay(buildings.values());
    }
    
    public void readFromNBT(NBTTagCompound nbt) {
        gathering.readFromNBT(nbt);
        NBTTagList list = nbt.getTagList("TownBuildingList", 10);
        for (int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound tag = list.getCompoundTagAt(i);
            String name = tag.getString("BuildingKey");
            TownBuilding building = new TownBuilding();
            building.readFromNBT(tag);
            buildings.put(new ResourceLocation(name), building);
        }
    }

    public void writeToNBT(NBTTagCompound nbt) {
        gathering.writeToNBT(nbt);
        NBTTagList list = new NBTTagList();
        for (ResourceLocation name : buildings.keySet()) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("BuildingKey", name.toString());
            buildings.get(name).writeToNBT(tag);
            list.appendTag(tag);
        }

        nbt.setTag("TownBuildingList", list);
    }
}
