package joshie.harvest.crops;

import gnu.trove.map.TIntObjectMap;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.ICalendarDate;
import joshie.harvest.api.calendar.Weekday;
import joshie.harvest.api.crops.ICrop;
import joshie.harvest.api.crops.ICropData;
import joshie.harvest.blocks.BlockCrop;
import joshie.harvest.blocks.BlockCrop.Stage;
import joshie.harvest.blocks.HFBlocks;
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
        Weekday day = HFTrackers.getCalendar().getDate().getWeekday();
        Iterator<Map.Entry<BlockPos, ICropData>> iter = getDimensionData(world).entrySet().iterator();
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

        //Dehydrate the farmland that a crop underneath it, Forcefully
        Iterator<BlockPos> it = getDimensionData(world).keySet().iterator();
        while (it.hasNext()) {
            BlockPos position = it.next();
            CropHelper.dehydrate(world, position.down(), world.getBlockState(position.down()));
        }

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
        if (!HFTrackers.getCalendar().getDate().equals(lastRain)) {
            lastRain = HFApi.calendar.cloneDate(HFTrackers.getCalendar().getDate());
            for (BlockPos position : getDimensionData(world).keySet()) {
                IBlockState state = world.getBlockState(position);
                hydrate(world, position, state);
                world.setBlockState(position.down(), state.withProperty(BlockFarmland.MOISTURE, 7), 2);
            }
        }
    }

    //Sends an update packet
    @Override
    public void sendUpdateToClient(EntityPlayerMP player, World world, BlockPos pos) {
        ICropData data = getDimensionData(world).get(pos);
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
        HFTrackers.markDirty();
    }

    @Override
    public boolean plantCrop(EntityPlayer player, World world, BlockPos pos, ICrop crop, int stage) {
        ICropData data = getCropDataForLocation(world, pos);
        if (CropHelper.isHydrated(world, pos.down())) {
            data.setHydrated();
        }

        data.setCrop(player, crop, stage);

        getDimensionData(world).put(pos, data);
        sendToEveryone(new PacketSyncCrop(world.provider.getDimension(), pos, data));
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
                HFTrackers.getServerPlayerTracker(player).getTracking().onHarvested(data.getCrop());
            }

            HFTrackers.markDirty();
            sendToEveryone(new PacketSyncCrop(world.provider.getDimension(), pos, data));
            return harvest;
        } else return null;
    }

    @Override
    public void hydrate(World world, BlockPos pos, IBlockState state) {
        getCropDataForLocation(world, pos).setHydrated();
        HFTrackers.markDirty();
    }

    @Override
    public void setWithered(World world, BlockPos pos, ICropData data) {
        if (data.getCrop().isDouble(data.getStage())) {
            world.setBlockState(pos.up(), HFBlocks.CROPS.getStateFromEnum(BlockCrop.Stage.WITHERED_DOUBLE), 2);
        }

        world.setBlockState(pos, HFBlocks.CROPS.getStateFromEnum(Stage.WITHERED), 2);
        plantCrop(null, world, pos, data.getCrop(), data.getStage());
    }

    @Override
    public void removeCrop(World world, BlockPos pos) {
        super.removeCrop(world, pos);
        sendToEveryone(new PacketSyncCrop(world.provider.getDimension(), pos));
        HFTrackers.markDirty();
    }

    public void readFromNBT(NBTTagCompound nbt) {
        NBTTagList crops = nbt.getTagList("CropData", 10);
        for (int i = 0; i < crops.tagCount(); i++) {
            NBTTagCompound tag = crops.getCompoundTagAt(i);
            int dimension = tag.getInteger("Dimension");
            HashMap<BlockPos, ICropData> cropMap = getDimensionData(dimension);
            NBTTagList dataList = tag.getTagList("Data", 10);
            for (int j = 0; j < dataList.tagCount(); j++) {
                NBTTagCompound data = dataList.getCompoundTagAt(j);
                BlockPos pos = new BlockPos(data.getInteger("X"), data.getInteger("Y"), data.getInteger("Z"));
                ICropData cropData = new CropData(pos, dimension);
                cropData.readFromNBT(data);
                cropMap.put(pos, cropData);
            }
        }
    }

    public void writeToNBT(NBTTagCompound nbt) {
        NBTTagList crops = new NBTTagList();
        TIntObjectMap<HashMap<BlockPos, ICropData>> map = this.dimensions;
        for (int entry: map.keys()) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setInteger("Dimension", entry);
            NBTTagList dataList = new NBTTagList();
            HashMap<BlockPos, ICropData> cropMap = map.get(entry);
            for (BlockPos pos: cropMap.keySet()) {
                NBTTagCompound data = new NBTTagCompound();
                data.setInteger("X", pos.getX());
                data.setInteger("Y", pos.getY());
                data.setInteger("Z", pos.getZ());
                cropMap.get(pos).writeToNBT(data);
                dataList.appendTag(data);
            }

            tag.setTag("Data", dataList);
            crops.appendTag(tag);
        }

        nbt.setTag("CropData", crops);
    }
}