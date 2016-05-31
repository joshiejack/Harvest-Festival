package joshie.harvest.crops;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.ICalendarDate;
import joshie.harvest.api.calendar.Weekday;
import joshie.harvest.api.crops.ICrop;
import joshie.harvest.api.crops.ICropData;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.helpers.CropHelper;
import joshie.harvest.core.helpers.NBTHelper;
import joshie.harvest.core.helpers.NBTHelper.ISaveable;
import joshie.harvest.core.network.PacketSyncCrop;
import joshie.harvest.crops.blocks.BlockHFCrops;
import joshie.harvest.crops.blocks.BlockHFCrops.Stage;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.IPlantable;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static joshie.harvest.core.network.PacketHandler.sendToClient;
import static joshie.harvest.core.network.PacketHandler.sendToEveryone;

//Handles the Data for the crops rather than using TE Data
public class CropTrackerServer extends CropTracker implements ISaveable {
    @Override
    public void newDay() {
        ArrayList<Pair<BlockPos, ICropData>> toWither = new ArrayList<>(); //Create a new wither list
        Weekday day = HFTrackers.getCalendar(getWorld()).getDate().getWeekday(getWorld());
        Iterator<Map.Entry<BlockPos, ICropData>> iter = cropTracker.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<BlockPos, ICropData> entry = iter.next();
            BlockPos position = entry.getKey();
            ICropData data = entry.getValue();
            boolean removed = false;
            if (day == Weekday.FRIDAY) {
                Block block = getWorld().getBlockState(position).getBlock();
                if (!(block instanceof IPlantable)) {
                    iter.remove();
                    removed = true; //We have removed this crop from our memory
                }
            }

            if (data.canGrow() && !removed) {
                boolean alive = data.newDay(getWorld());
                if (!alive) {
                    toWither.add(Pair.of(position, data));
                } else { //Send a packet with the new data to the clients
                    sendToEveryone(new PacketSyncCrop(getDimension(), position, data));
                }
            }
        }

        //Wither crops
        Iterator<Pair<BlockPos, ICropData>> wither = toWither.iterator();
        while (wither.hasNext()) {
            Pair<BlockPos, ICropData> data = wither.next();
            wither.remove();
            setWithered(data.getKey(), data.getValue());
        }
    }

    private ICalendarDate lastRain;

    //Updates the world, so we know it has rained!
    @Override
    public void doRain() {
        if (!HFTrackers.getCalendar(getWorld()).getDate().equals(lastRain)) {
            lastRain = HFApi.calendar.cloneDate(HFTrackers.getCalendar(getWorld()).getDate());
            for (BlockPos position : cropTracker.keySet()) {
                CropHelper.hydrate(getWorld(), position.down());
            }
        }
    }

    //Sends an update packet
    @Override
    public void sendUpdateToClient(EntityPlayerMP player, BlockPos pos) {
        ICropData data = cropTracker.get(pos);
        if (data == null) {
            sendToClient(new PacketSyncCrop(getDimension(), pos), player);
        } else sendToClient(new PacketSyncCrop(getDimension(), pos, data), player);
    }

    //Causes a growth of the crop at this location, Notifies the clients
    @Override
    public void grow(BlockPos pos) {
        ICropData data = getCropDataForLocation(pos);
        data.grow(getWorld());
        sendToEveryone(new PacketSyncCrop(getDimension(), pos, data));
        HFTrackers.markDirty(getDimension());
    }

    @Override
    public boolean plantCrop(EntityPlayer player, BlockPos pos, ICrop crop, int stage) {
        ICropData data = getCropDataForLocation(pos);
        if (CropHelper.isHydrated(getWorld(), pos.down())) {
            data.setHydrated();
        }

        data.setCrop(player, crop, stage);

        cropTracker.put(pos, data);
        sendToEveryone(new PacketSyncCrop(getDimension(), pos, data));
        HFTrackers.markDirty(getDimension());
        return true;
    }

    @Override
    public ItemStack harvest(EntityPlayer player, BlockPos pos) {
        ICropData data = getCropDataForLocation(pos);
        ItemStack harvest = data.harvest(player, true);
        if (harvest != null) {
            if (data.getCrop().getRegrowStage() <= 0) {
                removeCrop(pos);
                getWorld().setBlockToAir(pos);
            }

            if (player != null) {
                HFTrackers.getServerPlayerTracker(player).getTracking().onHarvested(data.getCrop());
            }

            HFTrackers.markDirty(getDimension());
            sendToEveryone(new PacketSyncCrop(getDimension(), pos, data));
            return harvest;
        } else return null;
    }

    @Override
    public void hydrate(BlockPos pos, IBlockState state) {
        getCropDataForLocation(pos).setHydrated();
        HFTrackers.markDirty(getDimension());
    }

    @Override
    public void setWithered(BlockPos pos, ICropData data) {
        if (data.getCrop().isDouble(data.getStage())) {
            getWorld().setBlockState(pos.up(), HFCrops.CROPS.getStateFromEnum(BlockHFCrops.Stage.WITHERED_DOUBLE), 2);
        }

        getWorld().setBlockState(pos, HFCrops.CROPS.getStateFromEnum(Stage.WITHERED), 2);
        plantCrop(null, pos, data.getCrop(), data.getStage());
    }

    @Override
    public void removeCrop(BlockPos pos) {
        super.removeCrop(pos);
        sendToEveryone(new PacketSyncCrop(getDimension(), pos));
        HFTrackers.markDirty(getDimension());
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        cropTracker = new HashMap<>();
        NBTTagList dataList = nbt.getTagList("Crops", 10);
        for (int j = 0; j < dataList.tagCount(); j++) {
            NBTTagCompound data = dataList.getCompoundTagAt(j);
            BlockPos pos = NBTHelper.readBlockPos("", data);
            ICropData cropData = new CropData(pos);
            cropData.readFromNBT(data);
            this.cropTracker.put(pos, cropData);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        NBTTagList dataList = new NBTTagList();
        for (BlockPos pos: cropTracker.keySet()) {
            NBTTagCompound data = new NBTTagCompound();
            NBTHelper.writeBlockPos("", data, pos);
            cropTracker.get(pos).writeToNBT(data);
            dataList.appendTag(data);
        }

        nbt.setTag("Crops", dataList);
        return nbt;
    }
}