package harvestmoon.crops;

import static harvestmoon.HarvestMoon.handler;
import static harvestmoon.network.PacketHandler.sendToClient;
import static harvestmoon.network.PacketHandler.sendToEveryone;
import harvestmoon.blocks.BlockSoil;
import harvestmoon.calendar.CalendarDate;
import harvestmoon.crops.CropData.WitherType;
import harvestmoon.init.HMBlocks;
import harvestmoon.network.PacketSyncCrop;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraftforge.common.DimensionManager;

//Handles the Data for the crops rather than using TE Data
public class CropTrackerServer extends WorldSavedData {
    public static final String DATA_NAME = "HM-Tracker-Crops";

    private static CalendarDate rain;
    private static HashMap<CropLocation, CropData> cropData = new HashMap();
    private static HashSet<CropLocation> farmland = new HashSet();

    public CropTrackerServer(String string) {
        super(string);
    }

    private CropLocation getKey(World world, int x, int y, int z) {
        return new CropLocation(world.provider.dimensionId, x, y, z);
    }

    //Set the crop at this location to be withered
    private void setWithered(CropLocation location, WitherType type) {
        DimensionManager.getWorld(location.dimension).setBlock(location.x, location.y, location.z, HMBlocks.withered, type.ordinal(), 2);
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
        while(it.hasNext()) {
            CropLocation location = it.next();
            if(!BlockSoil.dehydrate(DimensionManager.getWorld(location.dimension), location.x, location.y, location.z)) {
                it.remove();
                //Remove it from the loop ^, and then dehyrate the bitch
                DimensionManager.getWorld(location.dimension).setBlock(location.x, location.y, location.z, Blocks.dirt);
            }
        }

        markDirty();
        return true;
    }
    
    public void doRain() {
        if(!handler.getServer().getCalendar().getDate().equals(rain)) {
            for (CropLocation location : farmland) {
                BlockSoil.hydrate(DimensionManager.getWorld(location.dimension), location.x, location.y, location.z);
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
        markDirty();
        return world.setBlockToAir(x, y, z);
    }

    //Marks this plant as hydrated for the day
    public void hydrate(World world, int x, int y, int z) {
        CropData data = cropData.get(getKey(world, x, y, z));
        if (data != null) {
            data.setHydrated();
            markDirty();
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
        markDirty();
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

                markDirty();
                return ret;
            } else return null;
        }
    }

    public void addFarmland(World world, int x, int y, int z) {
        farmland.add(getKey(world, x, y, z));
        markDirty();
    }

    public void removeFarmland(World world, int x, int y, int z) {
        farmland.remove(getKey(world, x, y, z));
        markDirty();
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
