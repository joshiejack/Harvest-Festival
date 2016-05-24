package joshie.harvest.crops;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.ICalendarDate;
import joshie.harvest.api.calendar.Weekday;
import joshie.harvest.api.crops.ICrop;
import joshie.harvest.api.crops.ICropData;
import joshie.harvest.crops.blocks.BlockHFCrops;
import joshie.harvest.crops.blocks.BlockHFCrops.Stage;
import joshie.harvest.blocks.HFBlocks;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.helpers.CropHelper;
import joshie.harvest.core.helpers.NBTHelper;
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
import net.minecraftforge.common.IPlantable;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static joshie.harvest.core.network.PacketHandler.sendToClient;
import static joshie.harvest.core.network.PacketHandler.sendToEveryone;

//Handles the Data for the crops rather than using TE Data
public class CropTrackerServer extends CropTracker {
    @Override
    public void newDay(World world) {
        ArrayList<Pair<BlockPos, ICropData>> toWither = new ArrayList<>(); //Create a new wither list
        Weekday day = HFTrackers.getCalendar(world).getDate().getWeekday(world);
        Iterator<Map.Entry<BlockPos, ICropData>> iter = cropTracker.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<BlockPos, ICropData> entry = iter.next();
            BlockPos position = entry.getKey();
            ICropData data = entry.getValue();
            boolean removed = false;
            if (day == Weekday.FRIDAY) {
                Block block = world.getBlockState(position).getBlock();
                if (!(block instanceof IPlantable)) {
                    iter.remove();
                    removed = true; //We have removed this crop from our memory
                }
            }

            if (data.canGrow() && !removed) {
                boolean alive = data.newDay(world);
                if (!alive) {
                    toWither.add(Pair.of(position, data));
                } else { //Send a packet with the new data to the clients
                    sendToEveryone(new PacketSyncCrop(world.provider.getDimension(), position, data));
                }
            }
        }

        //Wither crops
        Iterator<Pair<BlockPos, ICropData>> wither = toWither.iterator();
        while (wither.hasNext()) {
            Pair<BlockPos, ICropData> data = wither.next();
            wither.remove();
            setWithered(world, data.getKey(), data.getValue());
        }
    }

    private ICalendarDate lastRain;

    //Updates the world, so we know it has rained!
    @Override
    public void doRain(World world) {
        if (!HFTrackers.getCalendar(world).getDate().equals(lastRain)) {
            lastRain = HFApi.calendar.cloneDate(HFTrackers.getCalendar(world).getDate());
            for (BlockPos position : cropTracker.keySet()) {
                IBlockState state = world.getBlockState(position);
                hydrate(world, position, state);
                world.setBlockState(position.down(), state.withProperty(BlockFarmland.MOISTURE, 7), 2);
            }
        }
    }

    //Sends an update packet
    @Override
    public void sendUpdateToClient(EntityPlayerMP player, World world, BlockPos pos) {
        ICropData data = cropTracker.get(pos);
        if (data == null) {
            sendToClient(new PacketSyncCrop(world.provider.getDimension(), pos), player);
        } else sendToClient(new PacketSyncCrop(world.provider.getDimension(), pos, data), player);
    }

    //Causes a growth of the crop at this location, Notifies the clients
    @Override
    public void grow(World world, BlockPos pos) {
        ICropData data = getCropDataForLocation(world, pos);
        data.grow(world);
        sendToEveryone(new PacketSyncCrop(world.provider.getDimension(), pos, data));
        HFTrackers.markDirty(world);
    }

    @Override
    public boolean plantCrop(EntityPlayer player, World world, BlockPos pos, ICrop crop, int stage) {
        ICropData data = getCropDataForLocation(world, pos);
        if (CropHelper.isHydrated(world, pos.down())) {
            data.setHydrated();
        }

        data.setCrop(player, crop, stage);

        cropTracker.put(pos, data);
        sendToEveryone(new PacketSyncCrop(world.provider.getDimension(), pos, data));
        HFTrackers.markDirty(world);
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
                HFTrackers.getServerPlayerTracker(player).getTracking().onHarvested(data.getCrop());
            }

            HFTrackers.markDirty(world);
            sendToEveryone(new PacketSyncCrop(world.provider.getDimension(), pos, data));
            return harvest;
        } else return null;
    }

    @Override
    public void hydrate(World world, BlockPos pos, IBlockState state) {
        getCropDataForLocation(world, pos).setHydrated();
        HFTrackers.markDirty(world);
    }

    @Override
    public void setWithered(World world, BlockPos pos, ICropData data) {
        if (data.getCrop().isDouble(data.getStage())) {
            world.setBlockState(pos.up(), HFBlocks.CROPS.getStateFromEnum(BlockHFCrops.Stage.WITHERED_DOUBLE), 2);
        }

        world.setBlockState(pos, HFBlocks.CROPS.getStateFromEnum(Stage.WITHERED), 2);
        plantCrop(null, world, pos, data.getCrop(), data.getStage());
    }

    @Override
    public void removeCrop(World world, BlockPos pos) {
        super.removeCrop(world, pos);
        sendToEveryone(new PacketSyncCrop(world.provider.getDimension(), pos));
        HFTrackers.markDirty(world);
    }

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