package joshie.harvest.core.handlers;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.ticking.IDailyTickable;
import joshie.harvest.api.ticking.IDailyTickable.Phase;
import joshie.harvest.api.ticking.IDailyTickableBlock;
import joshie.harvest.core.HFTracker;
import joshie.harvest.core.helpers.NBTHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

public class TickDailyServer extends HFTracker {
    private static final Set<Runnable> queue = new HashSet<>();
    private final Set<IDailyTickable> priority = new HashSet<>();
    private final Set<IDailyTickable> tickables = new HashSet<>();
    private final HashMap<BlockPos, IDailyTickableBlock> blockTicks = new HashMap<>();
    private long rateLimit;

    public static void addToQueue(Runnable runnable) {
        queue.add(runnable);
    }

    public static void processQueue() {
        queue.forEach((r) -> r.run());
    }

    public void newDay(Phase phase) {
        if (phase == Phase.MINE) {
            if (System.currentTimeMillis() - rateLimit < 10000) return;
            rateLimit = System.currentTimeMillis(); //Don't allow too man updates
        }

        //Process high priority tickables
        processTickables(priority, phase);

        //Tick the tickable blocks
        if (phase == Phase.PRE || phase == Phase.MINE) {
            Iterator<Entry<BlockPos, IDailyTickableBlock>> position = blockTicks.entrySet().iterator();
            while (position.hasNext()) {
                Entry<BlockPos, IDailyTickableBlock> entry = position.next();
                BlockPos pos = entry.getKey();
                if (pos == null) {
                    position.remove();
                } else if (getWorld().isBlockLoaded(pos)) {
                    IBlockState state = getWorld().getBlockState(pos);
                    IDailyTickableBlock tickable = entry.getValue();
                    if (tickable != null) {
                        if (phase == Phase.PRE || (phase == Phase.MINE && tickable.isMiningWorld()))
                        if (!tickable.newDay(getWorld(), pos, state)) {
                            position.remove();
                        }
                    } else position.remove(); //If this block isn't daily tickable, remove it
                }
            }
        }

        //Process the other tickables
        processTickables(tickables, phase);
    }

    public void processTickables(Set<IDailyTickable> tickables, Phase phase) {
        Iterator<IDailyTickable> ticking = tickables.iterator();
        while (ticking.hasNext()) {
            IDailyTickable tickable = ticking.next();
            if (tickable == null || ((TileEntity)tickable).isInvalid()) {
                ticking.remove();
            } else tickable.newDay(phase);
        }
    }

    public void add(final BlockPos pos, final IDailyTickableBlock daily) {
        blockTicks.put(pos, daily); HFTrackers.markDirty(getDimension());
    }

    public void remove(final BlockPos pos) {
        blockTicks.remove(pos); HFTrackers.markDirty(getDimension());
    }

    public void add(final IDailyTickable tickable) {
        if (tickable.isPriority()) priority.add(tickable);
        else tickables.add(tickable);
    }

    public void remove(final IDailyTickable tickable) {
        if (tickable.isPriority()) priority.remove(tickable);
        else tickables.remove(tickable);
    }

    @Override
    public void setWorld(World world) {
        super.setWorld(world);

        Set<BlockPos> positions = new HashSet<>(blockTicks.keySet());
        for (BlockPos pos: positions) {
            IDailyTickableBlock tickable = HFApi.tickable.getTickableFromBlock(getWorld().getBlockState(pos).getBlock());
            if (tickable != null) {
                blockTicks.put(pos, tickable); //Load in the tickables once the world is actually loaded
            } else blockTicks.remove(pos);
        }
    }

    public void readFromNBT(NBTTagCompound tag) {
        NBTTagList dataList = tag.getTagList("Positions", 10);
        for (int j = 0; j < dataList.tagCount(); j++) {
            NBTTagCompound data = dataList.getCompoundTagAt(j);
            blockTicks.put(NBTHelper.readBlockPos("", data), null);
        }
    }

    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        queue.forEach((r) -> r.run()); //Run the queue before saving
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
