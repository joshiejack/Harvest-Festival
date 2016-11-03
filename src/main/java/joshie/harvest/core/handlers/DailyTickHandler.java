package joshie.harvest.core.handlers;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import joshie.harvest.api.ticking.IDailyTickable;
import joshie.harvest.api.ticking.IDailyTickable.Phase;
import joshie.harvest.api.ticking.IDailyTickableBlock;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.util.HFTracker;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.*;
import java.util.Map.Entry;

public class DailyTickHandler extends HFTracker {
    private static final Set<Runnable> queue = new HashSet<>();
    private final Multimap<Phase, IDailyTickable> tickables = HashMultimap.create();
    private final HashMap<BlockPos, IDailyTickableBlock> blockTicks = new HashMap<>();

    public static void addToQueue(Runnable runnable) {
        queue.add(runnable);
    }

    static void processQueue() {
        Set<Runnable> toProcess = new HashSet<>(queue);
        queue.clear(); //Clear the old queue
        toProcess.forEach(Runnable :: run);
    }

    void processPhase(Phase phase) {
        processTickables(tickables.get(phase));
    }

    void processBlocks() {
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
                    if (!tickable.newDay(getWorld(), pos, state)) {
                        position.remove();
                    }
                } else position.remove(); //If this block isn't daily tickable, remove it
            }
        }
    }

    private void processTickables(Collection<IDailyTickable> tickables) {
        Iterator<IDailyTickable> ticking = tickables.iterator();
        while (ticking.hasNext()) {
            IDailyTickable tickable = ticking.next();
            if (tickable == null || ((TileEntity)tickable).isInvalid()) {
                ticking.remove();
            } else tickable.newDay();
        }
    }

    public void add(final BlockPos pos, final IDailyTickableBlock daily) {
        blockTicks.put(pos, daily); HFTrackers.markDirty(getDimension());
    }

    public void remove(final BlockPos pos) {
        blockTicks.remove(pos); HFTrackers.markDirty(getDimension());
    }

    public void add(final IDailyTickable tickable) {
        for (Phase phase: tickable.getPhases()) {
            tickables.get(phase).add(tickable);
        }
    }

    public void remove(final IDailyTickable tickable) {
        for (Phase phase: tickable.getPhases()) {
            tickables.get(phase).remove(tickable);
        }
    }
}
