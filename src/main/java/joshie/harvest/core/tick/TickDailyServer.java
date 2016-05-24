package joshie.harvest.core.tick;

import joshie.harvest.api.core.IDailyTickable;
import joshie.harvest.api.core.IDailyTickableBlock;
import joshie.harvest.core.HFTracker;
import joshie.harvest.core.helpers.NBTHelper;
import joshie.harvest.core.helpers.NBTHelper.ISaveable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class TickDailyServer extends HFTracker implements ISaveable {
    private Set<IDailyTickable> tickables = new HashSet<>();
    private Set<BlockPos> blockTicks = new HashSet<>();

    public void newDay() {
        //Tick the tickable entities
        Iterator<IDailyTickable> ticking = tickables.iterator();
        while (ticking.hasNext()) {
            IDailyTickable tickable = ticking.next();
            if (tickable == null || tickable.isInvalid()) {
                ticking.remove();
            } else tickable.newDay(getWorld());
        }

        //Tick the tickable blocks
        Iterator<BlockPos> position = blockTicks.iterator();
        while(position.hasNext()) {
            BlockPos pos = position.next();
            if (pos == null) {
                position.remove();
            } else if (getWorld().isBlockLoaded(pos)) {
                IBlockState state = getWorld().getBlockState(pos);
                if (state.getBlock() instanceof IDailyTickableBlock) {
                    if(!((IDailyTickableBlock) state.getBlock()).newDay(getWorld(), pos, state)) position.remove();
                } else position.remove(); //If this block isn't daily tickable, remove it
            }
        }
    }

    public void add(BlockPos pos) {
        blockTicks.add(pos);
    }

    public void remove(BlockPos pos) {
        blockTicks.remove(pos);
    }

    public void add(IDailyTickable tickable) {
        tickables.add(tickable);
    }

    public void remove(IDailyTickable tickable) {
        tickables.remove(tickable);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        blockTicks = new HashSet<>();
        NBTTagList dataList = tag.getTagList("Positions", 10);
        for (int j = 0; j < dataList.tagCount(); j++) {
            NBTTagCompound data = dataList.getCompoundTagAt(j);
            blockTicks.add(NBTHelper.readBlockPos("", data));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        NBTTagList dataList = new NBTTagList();
        for (BlockPos pos: blockTicks) {
            NBTTagCompound data = new NBTTagCompound();
            NBTHelper.writeBlockPos("", data, pos);
            dataList.appendTag(data);
        }

        tag.setTag("Positions", dataList);
        return tag;
    }
}
