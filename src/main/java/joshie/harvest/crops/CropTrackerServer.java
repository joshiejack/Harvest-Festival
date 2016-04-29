package joshie.harvest.crops;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.WorldLocation;
import joshie.harvest.api.calendar.ICalendarDate;
import joshie.harvest.api.calendar.Weekday;
import joshie.harvest.api.crops.ICrop;
import joshie.harvest.api.crops.ICropData;
import joshie.harvest.blocks.BlockCrop;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.helpers.CropHelper;
import joshie.harvest.core.network.PacketSyncCrop;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.IPlantable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import static joshie.harvest.core.helpers.generic.MCServerHelper.getWorld;
import static joshie.harvest.core.network.PacketHandler.sendToClient;
import static joshie.harvest.core.network.PacketHandler.sendToEveryone;

//Handles the Data for the crops rather than using TE Data
public class CropTrackerServer extends CropTracker {
    @Override
    public void newDay() {
        ArrayList<ICropData> toWither = new ArrayList<ICropData>(); //Create a new wither list
        Weekday day = HFTrackers.getCalendar().getDate().getWeekday();
        Iterator<Map.Entry<WorldLocation, ICropData>> iter = crops.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<WorldLocation, ICropData> entry = iter.next();
            ICropData data = entry.getValue();
            WorldLocation location = entry.getKey();
            World world = getWorld(location.dimension);

            boolean removed = false;
            if (day == Weekday.FRIDAY) {
                Block block = world.getBlockState(location.position).getBlock();
                if (!(block instanceof IPlantable)) {
                    iter.remove();
                    removed = true; //We have removed this crop from our memory
                }
            }

            if (data.canGrow() && !removed) {
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
            CropHelper.dehydrate(world, location.position.down(), world.getBlockState(location.position));
        }

        Iterator<ICropData> wither = toWither.iterator();
        while (wither.hasNext()) {
            ICropData data = wither.next();
            wither.remove();
            setWithered(data);
        }

        toWither = null;
    }

    private ICalendarDate lastRain;

    //Updates the world, so we know it has rained!
    @Override
    public void doRain() {
        if (!HFTrackers.getCalendar().getDate().equals(lastRain)) {
            lastRain = HFApi.CALENDAR.cloneDate(HFTrackers.getCalendar().getDate());
            for (WorldLocation location : crops.keySet()) {
                World world = getWorld(location.dimension);
                IBlockState state = world.getBlockState(location.position);
                hydrate(world, location.position, state);
                DimensionManager.getWorld(location.dimension).setBlockState(location.position.down(), state.withProperty(BlockFarmland.MOISTURE, 7), 2);
            }
        }
    }

    //Sends an update packet
    @Override
    public void sendUpdateToClient(EntityPlayerMP player, World world, BlockPos pos) {
        WorldLocation key = getCropKey(world, pos);
        ICropData data = crops.get(key);
        if (data == null) {
            sendToClient(new PacketSyncCrop(key), player);
        } else sendToClient(new PacketSyncCrop(key, data), player);
    }

    //Causes a growth of the crop at this location, Notifies the clients
    @Override
    public void grow(World world, BlockPos pos) {
        ICropData data = getCropDataForLocation(world, pos);
        data.grow();
        sendToEveryone(new PacketSyncCrop(data.getLocation(), data));
        HFTrackers.markDirty();
    }

    @Override
    public boolean plantCrop(EntityPlayer player, World world, BlockPos pos, ICrop crop, int stage) {
        ICropData data = getCropDataForLocation(world, pos);
        if (CropHelper.isHydrated(world, pos.down())) {
            data.setHydrated();
        }

        data.setCrop(player, crop, stage);

        crops.put(data.getLocation(), data);
        sendToEveryone(new PacketSyncCrop(data.getLocation(), data));
        HFTrackers.markDirty();
        return true;
    }

    @Override
    public ItemStack harvest(EntityPlayer player, World world, BlockPos pos) {
        ICropData data = getCropDataForLocation(world, pos);
        ItemStack harvest = data.harvest(player, true);
        if (harvest != null) {
            if (data.getCrop().getRegrowStage() <= 0) {
                removeCrop(world, pos);
                world.setBlockToAir(pos);
            }

            if (player != null) {
                HFTrackers.getServerPlayerTracker(player).getTracking().onHarvested(data);
            }

            HFTrackers.markDirty();
            sendToEveryone(new PacketSyncCrop(data.getLocation(), data));
            return harvest;
        } else return null;
    }

    @Override
    public void hydrate(World world, BlockPos pos, IBlockState state) {
        getCropDataForLocation(world, pos).setHydrated();
        HFTrackers.markDirty();
    }

    @Override
    public void setWithered(ICropData data) {
        WorldLocation location = data.getLocation();
        if (data.getCrop().isDouble(data.getStage())) {
            getWorld(location.dimension).setBlockState(location.position.up(), BlockCrop.WITHERED_DOUBLE, 2);
        }

        getWorld(location.dimension).setBlockState(location.position, BlockCrop.WITHERED, 2);
        plantCrop(null, getWorld(location.dimension), location.position, data.getCrop(), data.getStage());
    }

    @Override
    public void removeCrop(World world, BlockPos pos) {
        super.removeCrop(world, pos);
        HFTrackers.markDirty();
    }

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