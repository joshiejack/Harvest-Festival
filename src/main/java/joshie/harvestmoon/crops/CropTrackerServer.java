package joshie.harvestmoon.crops;

import static joshie.harvestmoon.core.helpers.ServerHelper.markDirty;
import static joshie.harvestmoon.core.helpers.generic.MCServerHelper.getWorld;
import static joshie.harvestmoon.core.network.PacketHandler.sendToClient;
import static joshie.harvestmoon.core.network.PacketHandler.sendToEveryone;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import joshie.harvestmoon.animals.AnimalType;
import joshie.harvestmoon.api.WorldLocation;
import joshie.harvestmoon.api.crops.ICrop;
import joshie.harvestmoon.api.crops.ICropData;
import joshie.harvestmoon.calendar.CalendarDate;
import joshie.harvestmoon.calendar.Weekday;
import joshie.harvestmoon.core.helpers.AnimalHelper;
import joshie.harvestmoon.core.helpers.CalendarHelper;
import joshie.harvestmoon.core.helpers.CropHelper;
import joshie.harvestmoon.core.helpers.TrackingHelper;
import joshie.harvestmoon.core.network.PacketSyncCrop;
import joshie.harvestmoon.core.util.IData;
import joshie.harvestmoon.init.HMBlocks;
import net.minecraft.block.Block;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.IPlantable;

//Handles the Data for the crops rather than using TE Data
public class CropTrackerServer extends CropTrackerCommon implements IData {
    public void newDay() {
        ArrayList<ICropData> toWither = new ArrayList(); //Create a new wither list
        Weekday day = CalendarHelper.getWeekday(DimensionManager.getWorld(0));
        Iterator<Map.Entry<WorldLocation, ICropData>> iter = crops.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<WorldLocation, ICropData> entry = iter.next();
            ICropData data = entry.getValue();
            WorldLocation location = entry.getKey();
            World world = getWorld(location.dimension);

            boolean removed = false;
            if (day == Weekday.FRIDAY) {
                Block block = world.getBlock(location.x, location.y, location.z);
                if (!(block instanceof IPlantable)) {
                    iter.remove();
                    removed = true; //We have removed this crop from our memory
                }
            }

            if (data.canGrow() && !removed) {
                //Feed surrounding animals with grass
                if (data.getCrop().isEdible()) {
                    ArrayList<EntityAnimal> animals = (ArrayList<EntityAnimal>) world.getEntitiesWithinAABB(EntityAnimal.class, HMBlocks.cookware.getCollisionBoundingBoxFromPool(world, location.x, location.y, location.z).expand(16D, 16D, 16D));
                    for (EntityAnimal animal : animals) {
                        if (AnimalType.getType(animal).eatsGrass()) {
                            AnimalHelper.feed(null, animal);
                        }
                    }
                }

                boolean alive = data.newDay();
                if (!alive) {
                    toWither.add(data);
                } else { //Send a packet with the new data to the clients
                    sendToEveryone(new PacketSyncCrop(location, data));
                }
            }
        }

        //Dehydrate the farmland that a crop underneath it, Forcefully
        Iterator<WorldLocation> it = crops.keySet().iterator();
        while (it.hasNext()) {
            WorldLocation location = it.next();
            World world = getWorld(location.dimension);
            CropHelper.dehydrate(world, location.x, location.y - 1, location.z);
        }

        Iterator<ICropData> wither = toWither.iterator();
        while (wither.hasNext()) {
            ICropData data = wither.next();
            wither.remove();
            setWithered(data);
        }

        toWither = null;
    }

    private CalendarDate lastRain;

    //Updates the world, so we know it has rained!
    public void doRain() {
        if (!CalendarHelper.getServerDate().equals(lastRain)) {
            for (WorldLocation location : crops.keySet()) {
                hydrate(getWorld(location.dimension), location.x, location.y, location.z);
            }
        }
    }

    //Sends an update packet
    public void sendUpdateToClient(EntityPlayerMP player, World world, int x, int y, int z) {
        WorldLocation key = getCropKey(world, x, y, z);
        ICropData data = crops.get(key);
        if (data == null) {
            sendToClient(new PacketSyncCrop(key), player);
        } else sendToClient(new PacketSyncCrop(key, data), player);
    }

    //Causes a growth of the crop at this location, Notifies the clients
    public void grow(World world, int x, int y, int z) {
        ICropData data = getCropDataForLocation(world, x, y, z);
        int stage = data.getStage() + 1;
        plantCrop(null, world, x, y, z, data.getCrop(), data.getQuality(), stage);
    }

    @Override
    public boolean plantCrop(EntityPlayer player, World world, int x, int y, int z, ICrop crop, int quality, int stage) {
        ICropData data = getCropDataForLocation(world, x, y, z);
        if (CropHelper.isHydrated(world, x, y - 1, z)) {
            data.setHydrated();
        }

        data.setCrop(player, crop, quality, stage);

        crops.put(data.getLocation(), data);
        sendToEveryone(new PacketSyncCrop(data.getLocation(), data));
        markDirty();
        return true;
    }

    @Override
    public ItemStack harvest(EntityPlayer player, World world, int x, int y, int z) {
        ICropData data = getCropDataForLocation(world, x, y, z);
        ItemStack harvest = data.harvest(player, true);
        if (harvest != null) {
            if (data.getCrop().getRegrowStage() <= 0) {
                removeCrop(world, x, y, z);
                world.setBlockToAir(x, y, z);
            }

            if (player != null) {
                TrackingHelper.onHarvested(player, data);
            }

            markDirty();
            sendToEveryone(new PacketSyncCrop(data.getLocation(), (CropData) data));
            return harvest;
        } else return null;
    }

    @Override
    public void hydrate(World world, int x, int y, int z) {
        getCropDataForLocation(world, x, y, z).setHydrated();
        markDirty();
    }

    @Override
    public void setWithered(ICropData data) {
        WorldLocation location = data.getLocation();
        getWorld(location.dimension).setBlockMetadataWithNotify(location.x, location.y, location.z, 2, 2);
        plantCrop(null, getWorld(location.dimension), location.x, location.y, location.z, data.getCrop(), data.getQuality(), data.getStage());
    }

    @Override
    public void removeCrop(World world, int x, int y, int z) {
        super.removeCrop(world, x, y, z);
        markDirty();
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        NBTTagList crops = nbt.getTagList("CropData", 10);
        for (int i = 0; i < crops.tagCount(); i++) {
            NBTTagCompound tag = crops.getCompoundTagAt(i);
            WorldLocation location = new WorldLocation();
            location.readFromNBT(tag);
            ICropData data = new CropData(location);
            data.readFromNBT(tag);
            this.crops.put(location, data);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        NBTTagList crops = new NBTTagList();
        for (Map.Entry<WorldLocation, ICropData> entry : this.crops.entrySet()) {
            NBTTagCompound tag = new NBTTagCompound();
            entry.getKey().writeToNBT(tag);
            entry.getValue().writeToNBT(tag);
            crops.appendTag(tag);
        }

        nbt.setTag("CropData", crops);
    }
}
