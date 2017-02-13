package joshie.harvest.core.handlers;

import joshie.harvest.api.ticking.DailyTickableBlock;
import joshie.harvest.api.ticking.DailyTickableBlock.Phases;
import joshie.harvest.core.util.HFTracker;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class DailyTickHandler extends HFTracker {
    private final Map<BlockPos, DailyTickableBlock> queue = new HashMap<>();
    private final Map<Phases, Map<BlockPos, DailyTickableBlock>> blockTickables = new HashMap<>();

    public DailyTickHandler() {
        for (Phases phase: Phases.values()) blockTickables.put(phase, new HashMap<>());
    }

    //Add to the queue
    public void add(final BlockPos pos, final DailyTickableBlock daily) {
        queue.put(pos, daily);
    }

    void processQueue() {
        for (Entry<BlockPos, DailyTickableBlock> entry: queue.entrySet()) {
            DailyTickableBlock daily = entry.getValue();
            for (Phases phase : daily.getPhases()) {
                blockTickables.get(phase).put(entry.getKey(), daily);
            }
        }

        queue.clear(); //Clear the old queue
    }

    void processPhase(Phases phase) {
        processTickableBlocks(blockTickables.get(phase));
    }

    @SuppressWarnings("unchecked")
    private void processTickableBlocks(Map<BlockPos, DailyTickableBlock> tickables) {
        World world = getWorld();
        Iterator<Entry<BlockPos, DailyTickableBlock>> entries = tickables.entrySet().iterator();
        while(entries.hasNext()) {
            Entry<BlockPos, DailyTickableBlock> entry = entries.next();
            BlockPos pos = entry.getKey();
            if (world.isBlockLoaded(pos)) {
                IBlockState state = world.getBlockState(pos);
                DailyTickableBlock tickable = entry.getValue();
                if (tickable.isStateCorrect(world, pos, state)) {
                    tickable.newDay(world, pos, state);
                } else entries.remove();
            }
        }
    }
}
