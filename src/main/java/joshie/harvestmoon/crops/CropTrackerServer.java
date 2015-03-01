package joshie.harvestmoon.crops;

import static joshie.harvestmoon.core.helpers.ServerHelper.markDirty;
import static joshie.harvestmoon.core.helpers.generic.MCServerHelper.getWorld;
import static joshie.harvestmoon.core.network.PacketHandler.sendToClient;
import static joshie.harvestmoon.core.network.PacketHandler.sendToEveryone;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import joshie.harvestmoon.animals.AnimalType;
import joshie.harvestmoon.api.crops.ICropData;
import joshie.harvestmoon.calendar.CalendarDate;
import joshie.harvestmoon.core.helpers.AnimalHelper;
import joshie.harvestmoon.core.helpers.CalendarHelper;
import joshie.harvestmoon.core.helpers.CropHelper;
import joshie.harvestmoon.core.network.PacketSyncCrop;
import joshie.harvestmoon.core.util.IData;
import joshie.harvestmoon.crops.CropData.WitherType;
import joshie.harvestmoon.init.HMBlocks;
import joshie.harvestmoon.init.HMItems;
import net.minecraft.entity.passive.EntityAnimal;
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
    private HashMap<WorldLocation, CropData> cropData = new HashMap();

    private WorldLocation getFarmlandKey(World world, int x, int y, int z) {
        return new WorldLocation(world.provider.dimensionId, x, y, z);
    }

    private WorldLocation getCropKey(World world, int x, int y, int z) {
        return new WorldLocation(world.provider.dimensionId, x, y - 1, z);
    }

    //Set the crop at this location to be withered
    private void setWithered(WorldLocation location, WitherType type) {
        getWorld(location.dimension).setBlock(location.x, location.y, location.z, HMBlocks.withered, type.ordinal(), 2);
        //Lets the clients know that this crop was destroyed
        sendToEveryone(new PacketSyncCrop(location));
    }

    public boolean newDay() {
        Iterator<Map.Entry<WorldLocation, CropData>> iter = cropData.entrySet().iterator();

        while (iter.hasNext()) {
            Map.Entry<WorldLocation, CropData> entry = iter.next();
            CropData data = entry.getValue();
            if (data.canGrow()) {
                WorldLocation location = entry.getKey();
                //Feed surrounding animals with grass
                if (data.isEdible()) {
                    World world = getWorld(location.dimension);
                    ArrayList<EntityAnimal> animals = (ArrayList<EntityAnimal>) world.getEntitiesWithinAABB(EntityAnimal.class, HMBlocks.cookware.getCollisionBoundingBoxFromPool(world, location.x, location.y, location.z).expand(16D, 16D, 16D));
                    for (EntityAnimal animal : animals) {
                        if (AnimalType.getType(animal).eatsGrass()) {
                            AnimalHelper.feed(null, animal);
                        }
                    }
                }

                WitherType wither = data.newDay();
                if (wither != WitherType.NONE) {
                    iter.remove();
                    setWithered(location, wither);
                } else { //Send a packet with the new data to the clients
                    sendToEveryone(new PacketSyncCrop(location, data));
                }
            }
        }

        //Dehydrate the farmland
        Iterator<WorldLocation> it = cropData.keySet().iterator();
        while (it.hasNext()) {
            WorldLocation location = it.next();
            if (!CropHelper.dehydrate(getWorld(location.dimension), location.x, location.y, location.z)) {
                it.remove();
                //Remove it from the loop ^, and then dehyrate the bitch
                getWorld(location.dimension).setBlock(location.x, location.y, location.z, Blocks.dirt);
            }
        }

        return true;
    }

    public void doRain() {
        if (!CalendarHelper.getServerDate().equals(rain)) {
            for (WorldLocation location : cropData.keySet()) {
                CropHelper.hydrate(getWorld(location.dimension), location.x, location.y, location.z);
            }
        }
    }

    //Sends an update packet
    public void sendUpdateToClient(EntityPlayerMP player, World world, int x, int y, int z) {
        WorldLocation key = getCropKey(world, x, y, z);
        CropData data = cropData.get(key);
        if (data == null) {
            sendToClient(new PacketSyncCrop(key), player);
        } else sendToClient(new PacketSyncCrop(key, data), player);
    }

    //Causes a growth of the crop at this location, Notifies the clients
    public void grow(World world, int x, int y, int z) {
        WorldLocation location = getCropKey(world, x, y, z);
        CropData data = cropData.get(location);
        if (data != null) {
            data.grow();
            sendToEveryone(new PacketSyncCrop(location, data));
        }
    }

    //Marks this plant as hydrated for the day
    public void hydrate(World world, int x, int y, int z) {
        CropData data = cropData.get(getCropKey(world, x, y, z));
        if (data != null) {
            data.setHydrated();
            markDirty();
        }
    }

    //Plants a crop at the location, PLAYER CAN BE NULL
    public boolean plant(EntityPlayer player, World world, int x, int y, int z, Crop crop, int quality) {
        WorldLocation key = getCropKey(world, x, y, z);
        CropData data = new CropData(player, crop, quality, key);
        if (CropHelper.isHydrated(world, x, y - 1, z)) {
            data.setHydrated();
        }

        cropData.put(key, data);
        sendToEveryone(new PacketSyncCrop(key, data));
        markDirty();
        return true;
    }

    //Harvests a crop at the location, PLAYER CAN BE NULL
    public ItemStack harvest(EntityPlayer player, World world, int x, int y, int z) {
        WorldLocation key = getCropKey(world, x, y, z);
        CropData data = cropData.get(key);
        if (data == null) return null;
        else {
            ItemStack ret = data.harvest();
            if (ret != null) {
                if (!data.doesRegrow()) {
                    data.clear();
                    world.setBlockToAir(x, y, z);
                }

                sendToEveryone(new PacketSyncCrop(key, data));
                if (player != null) {
                    CropHelper.onHarvested(player, data);
                }

                markDirty();
                return ret;
            } else return null;
        }
    }

    public void removeCrop(World world, int x, int y, int z) {
        WorldLocation key = getCropKey(world, x, y, z);
        CropData data = cropData.get(key);
        if (data == null) return;
        else data.clear();
    }

    public ItemStack getStackForCrop(World world, int x, int y, int z) {
        WorldLocation key = getCropKey(world, x, y, z);
        CropData data = cropData.get(key);
        if (data == null) return new ItemStack(Blocks.cactus); //Because why not?
        ItemStack seeds = new ItemStack(HMItems.seeds);
        seeds.setItemDamage(data.getCrop().getCropMeta() + ((data.getQuality() - 1) * 100));
        return seeds;
    }

    public boolean canBonemeal(World world, int x, int y, int z) {
        WorldLocation key = getCropKey(world, x, y, z);
        CropData data = cropData.get(key);
        if (data == null) return false;
        return data.getStage() < data.getCrop().getStages();
    }

    public ICropData getCropAtLocation(World world, int x, int y, int z) {
        return cropData.get(getCropKey(world, x, y, z));
    }

    public void addFarmland(World world, int x, int y, int z) {
        cropData.put(getFarmlandKey(world, x, y, z), new CropData(getFarmlandKey(world, x, y, z)));
        markDirty();
    }

    public void removeFarmland(World world, int x, int y, int z) {
        cropData.remove(getFarmlandKey(world, x, y, z));
        markDirty();
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        NBTTagList crops = nbt.getTagList("CropData", 10);
        for (int i = 0; i < crops.tagCount(); i++) {
            NBTTagCompound tag = crops.getCompoundTagAt(i);
            WorldLocation location = new WorldLocation();
            location.readFromNBT(tag);
            CropData data = new CropData(location);
            data.readFromNBT(tag);
            cropData.put(location, data);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        NBTTagList crops = new NBTTagList();
        for (Map.Entry<WorldLocation, CropData> entry : cropData.entrySet()) {
            NBTTagCompound tag = new NBTTagCompound();
            entry.getKey().writeToNBT(tag);
            entry.getValue().writeToNBT(tag);
            crops.appendTag(tag);
        }

        nbt.setTag("CropData", crops);
    }
}
