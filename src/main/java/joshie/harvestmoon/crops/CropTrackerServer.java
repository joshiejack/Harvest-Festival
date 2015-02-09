package joshie.harvestmoon.crops;

import static joshie.harvestmoon.HarvestMoon.handler;
import static joshie.harvestmoon.helpers.generic.ServerHelper.getWorld;
import static joshie.harvestmoon.network.PacketHandler.sendToClient;
import static joshie.harvestmoon.network.PacketHandler.sendToEveryone;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import joshie.harvestmoon.blocks.BlockSoil;
import joshie.harvestmoon.calendar.CalendarDate;
import joshie.harvestmoon.crops.CropData.WitherType;
import joshie.harvestmoon.init.HMBlocks;
import joshie.harvestmoon.network.PacketSyncCrop;
import joshie.harvestmoon.util.IData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

//Handles the Data for the crops rather than using TE Data
public class CropTrackerServer implements IData {
    private CalendarDate rain;
    private HashMap<CropLocation, CropData> cropData = new HashMap();
    private HashSet<CropLocation> farmland = new HashSet();

    private CropLocation getKey(World world, int x, int y, int z) {
        return new CropLocation(world.provider.dimensionId, x, y, z);
    }

    //Set the crop at this location to be withered
    private void setWithered(CropLocation location, WitherType type) {
        getWorld(location.dimension).setBlock(location.x, location.y, location.z, HMBlocks.withered, type.ordinal(), 2);
        //Lets the clients know that this crop was destroyed
        sendToEveryone(new PacketSyncCrop(location));
    }

    public boolean newDay() {
        Iterator<Map.Entry<CropLocation, CropData>> iter = cropData.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<CropLocation, CropData> entry = iter.next();
            CropData data = entry.getValue();
            CropLocation location = entry.getKey();
            WitherType wither = data.newDay();
            if (wither != WitherType.NONE) {
                iter.remove();
                setWithered(location, wither);
            } else { //Send a packet with the new data to the clients
                sendToEveryone(new PacketSyncCrop(location, data));
            }
        }

        //Dehydrate the farmland
        Iterator<CropLocation> it = farmland.iterator();
        while (it.hasNext()) {
            CropLocation location = it.next();
            if (!BlockSoil.dehydrate(getWorld(location.dimension), location.x, location.y, location.z)) {
                it.remove();
                //Remove it from the loop ^, and then dehyrate the bitch
                getWorld(location.dimension).setBlock(location.x, location.y, location.z, Blocks.dirt);
            }
        }

        return true;
    }

    public void doRain() {
        if (!handler.getServer().getCalendar().getDate().equals(rain)) {
            for (CropLocation location : farmland) {
                BlockSoil.hydrate(getWorld(location.dimension), location.x, location.y, location.z);
            }
        }
    }

    //Sends an update packet
    public void sendUpdateToClient(EntityPlayerMP player, World world, int x, int y, int z) {
        CropLocation key = getKey(world, x, y, z);
        CropData data = cropData.get(key);
        if (data == null) {
            sendToClient(new PacketSyncCrop(key), player);
        } else sendToClient(new PacketSyncCrop(key, data), player);
    }

    //Removes a crop at the location, PLAYER CAN BE NULL
    public boolean destroy(EntityPlayer player, World world, int x, int y, int z) {
        CropLocation key = getKey(world, x, y, z);
        cropData.remove(key);
        sendToEveryone(new PacketSyncCrop(key));
        handler.getServer().markDirty();
        return world.setBlockToAir(x, y, z);
    }

    //Marks this plant as hydrated for the day
    public void hydrate(World world, int x, int y, int z) {
        CropData data = cropData.get(getKey(world, x, y, z));
        if (data != null) {
            data.setHydrated();
            handler.getServer().markDirty();
        }
    }

    //Plants a crop at the location, PLAYER CAN BE NULL
    public boolean plant(EntityPlayer player, World world, int x, int y, int z, Crop crop, int quality) {
        CropLocation key = getKey(world, x, y, z);
        CropData data = new CropData(crop, quality);
        world.setBlock(x, y, z, HMBlocks.crops);
        if (BlockSoil.isHydrated(world, x, y - 1, z)) {
            data.setHydrated();
        }

        cropData.put(key, data);
        sendToEveryone(new PacketSyncCrop(key, data));
        handler.getServer().markDirty();
        return true;
    }

    //Harvests a crop at the location, PLAYER CAN BE NULL
    public ItemStack harvest(EntityPlayer player, World world, int x, int y, int z) {
        CropLocation key = getKey(world, x, y, z);
        CropData data = cropData.get(key);
        if (data == null) return null;
        else {
            ItemStack ret = data.harvest();
            if (ret != null) {
                if (!data.doesRegrow()) {
                    cropData.remove(key);
                    world.setBlockToAir(x, y, z);
                    sendToEveryone(new PacketSyncCrop(key, data));
                } else sendToEveryone(new PacketSyncCrop(key));

                if (player != null) {
                    handler.getServer().getPlayerData(player).onHarvested(data);
                }

                handler.getServer().markDirty();
                return ret;
            } else return null;
        }
    }

    public void addFarmland(World world, int x, int y, int z) {
        farmland.add(getKey(world, x, y, z));
        handler.getServer().markDirty();
    }

    public void removeFarmland(World world, int x, int y, int z) {
        farmland.remove(getKey(world, x, y, z));
        handler.getServer().markDirty();
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        NBTTagList crops = nbt.getTagList("CropData", 10);
        for (int i = 0; i < crops.tagCount(); i++) {
            NBTTagCompound tag = crops.getCompoundTagAt(i);
            CropLocation location = new CropLocation();
            location.readFromNBT(tag);
            CropData data = new CropData();
            data.readFromNBT(tag);
            cropData.put(location, data);
        }

        //Farmland
        NBTTagList farm = nbt.getTagList("Farmland", 10);
        for (int i = 0; i < farm.tagCount(); i++) {
            NBTTagCompound tag = farm.getCompoundTagAt(i);
            CropLocation location = new CropLocation();
            location.readFromNBT(tag);
            farmland.add(location);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        NBTTagList crops = new NBTTagList();
        for (Map.Entry<CropLocation, CropData> entry : cropData.entrySet()) {
            NBTTagCompound tag = new NBTTagCompound();
            entry.getKey().writeToNBT(tag);
            entry.getValue().writeToNBT(tag);
            crops.appendTag(tag);
        }

        nbt.setTag("CropData", crops);

        //Farmland
        NBTTagList farm = new NBTTagList();
        for (CropLocation location : farmland) {
            NBTTagCompound tag = new NBTTagCompound();
            location.writeToNBT(tag);
            farm.appendTag(tag);
        }

        nbt.setTag("Farmland", farm);
    }
}
