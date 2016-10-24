package joshie.harvest.core.handlers;

import joshie.harvest.api.ticking.IDailyTickable;
import joshie.harvest.api.ticking.IDailyTickable.Phase;
import joshie.harvest.api.ticking.IDailyTickableBlock;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.util.HFTracker;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

public class DailyTickHandler extends HFTracker {
    private static final Set<Runnable> queue = new HashSet<>();
    private final Set<IDailyTickable> priority = new HashSet<>();
    private final Set<IDailyTickable> tickables = new HashSet<>();
    private final HashMap<BlockPos, IDailyTickableBlock> blockTicks = new HashMap<>();
    private long rateLimit;

    public static void addToQueue(Runnable runnable) {
        queue.add(runnable);
    }

    public static void processQueue() {
        Set<Runnable> toProcess = new HashSet<>(queue);
        queue.clear(); //Clear the old queue
        toProcess.forEach(Runnable :: run);
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
            World world = getWorld();
            Iterator<Entry<BlockPos, IDailyTickableBlock>> position = blockTicks.entrySet().iterator();
            while (position.hasNext()) {
                Entry<BlockPos, IDailyTickableBlock> entry = position.next();
                BlockPos pos = entry.getKey();
                if (pos == null) {
                    position.remove();
                } else if (world.isBlockLoaded(pos)) {
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
}
