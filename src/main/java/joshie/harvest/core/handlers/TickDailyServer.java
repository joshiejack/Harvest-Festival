package joshie.harvest.core.handlers;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.core.IDailyTickable;
import joshie.harvest.api.core.IDailyTickableBlock;
import joshie.harvest.core.HFTracker;
import joshie.harvest.core.helpers.NBTHelper;
import joshie.harvest.core.helpers.NBTHelper.ISaveable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

public class TickDailyServer extends HFTracker implements ISaveable {
    private Set<IDailyTickable> tickables = new HashSet<>();
    private HashMap<BlockPos, IDailyTickableBlock> blockTicks = new HashMap<>();

    public void newDay() {
        //Tick the tickable entities
        Set<IDailyTickable> invalid = new HashSet<>();
        Iterator<IDailyTickable> ticking = tickables.iterator();
        while (ticking.hasNext()) {
            IDailyTickable tickable = ticking.next();
            if (tickable == null || tickable.isInvalid()) {
                invalid.add(tickable);
                ticking.remove();
            } else tickable.newDay(getWorld());
        }

        //Remove the invalid states
        for (IDailyTickable tickable: invalid) {
            tickable.onInvalidated();
        }

        //Tick the tickable blocks
        Iterator<Entry<BlockPos, IDailyTickableBlock>> position = blockTicks.entrySet().iterator();
        while(position.hasNext()) {
            Entry<BlockPos, IDailyTickableBlock> entry = position.next();
            BlockPos pos = entry.getKey();
            if (pos == null) {
                position.remove();
            } else if (getWorld().isBlockLoaded(pos)) {
                IBlockState state = getWorld().getBlockState(pos);
                IDailyTickableBlock tickable = entry.getValue();
                if (tickable != null) {
                    if(!tickable.newDay(getWorld(), pos, state)) {
                        position.remove();
                    }
                } else position.remove(); //If this block isn't daily tickable, remove it
            }
        }
    }

    public void add(BlockPos pos, IDailyTickableBlock daily) {
        blockTicks.put(pos, daily);
        HFTrackers.markDirty(getDimension());
    }

    public void remove(BlockPos pos) {
        blockTicks.remove(pos);
        HFTrackers.markDirty(getDimension());
    }

    public void add(IDailyTickable tickable) {
        tickables.add(tickable);
    }

    public void remove(IDailyTickable tickable) {
        tickables.remove(tickable);
    }

    @Override
    public void setWorld(World world) {
        super.setWorld(world);

        Set<BlockPos> positions = new HashSet(blockTicks.keySet());
        for (BlockPos pos: positions) {
            IDailyTickableBlock tickable = HFApi.tickable.getTickableFromBlock(getWorld().getBlockState(pos).getBlock());
            if (tickable != null) {
                blockTicks.put(pos, tickable); //Load in the tickables once the world is actually loaded
            } else blockTicks.remove(pos);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        blockTicks = new HashMap<>();
        NBTTagList dataList = tag.getTagList("Positions", 10);
        for (int j = 0; j < dataList.tagCount(); j++) {
            NBTTagCompound data = dataList.getCompoundTagAt(j);
            blockTicks.put(NBTHelper.readBlockPos("", data), null);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        NBTTagList dataList = new NBTTagList();
        for (BlockPos pos: blockTicks.keySet()) {
            NBTTagCompound data = new NBTTagCompound();
            NBTHelper.writeBlockPos("", data, pos);
            dataList.appendTag(data);
        }

        tag.setTag("Positions", dataList);
        return tag;
    }
}
