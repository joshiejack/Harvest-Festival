package joshie.harvest.player.town;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class TownDataServer extends TownData {
    //TODO: SYNC THE TOWN DATA TO THE CLIENT
    public void sync (EntityPlayerMP player) {
        
    }
    
    public void readFromNBT(NBTTagCompound nbt) {
        NBTTagList list = nbt.getTagList("TownBuildingList", 10);
        for (int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound tag = list.getCompoundTagAt(i);
            String name = tag.getString("BuildingKey");
            TownBuilding building = new TownBuilding();
            building.readFromNBT(tag);
            buildings.put(name, building);
        }
    }

    public void writeToNBT(NBTTagCompound nbt) {
        NBTTagList list = new NBTTagList();
        for (String name : buildings.keySet()) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("BuildingKey", name);
            buildings.get(name).writeToNBT(tag);
            list.appendTag(tag);
        }

        nbt.setTag("TownBuildingList", list);
    }
}
