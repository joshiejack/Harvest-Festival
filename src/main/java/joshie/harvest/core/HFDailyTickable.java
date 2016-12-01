package joshie.harvest.core;

import joshie.harvest.api.ticking.IDailyTickable;
import joshie.harvest.api.ticking.IDailyTickableBlock;
import joshie.harvest.api.ticking.IDailyTickableRegistry;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.handlers.TickDailyServer;
import joshie.harvest.core.util.HFApiImplementation;
import joshie.harvest.crops.FarmlandTickable;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;

@HFApiImplementation
public class HFDailyTickable implements IDailyTickableRegistry {
    public static final HFDailyTickable INSTANCE = new HFDailyTickable();
    private final HashMap<Block, IDailyTickableBlock> registry = new HashMap<>();
    public FarmlandTickable farmland = new FarmlandTickable();

    private HFDailyTickable() {
        registry.put(Blocks.FARMLAND, farmland);
    }

    @Override
    public void registerDailyTickableBlock(Block block, IDailyTickableBlock daily) {
        registry.put(block, daily);
    }

    @Override
    public void addTickable(World world, IDailyTickable tickable) {
        if (!world.isRemote) {
            TickDailyServer.addToQueue(() -> HFTrackers.getTickables(world).add(tickable));
        }
    }

    @Override
    public void addTickable(World world, BlockPos pos, IDailyTickableBlock tickable) {
        if (!world.isRemote) {
            TickDailyServer.addToQueue(() -> HFTrackers.getTickables(world).add(pos, tickable));
        }
    }

    @Override
    public void removeTickable(World world, IDailyTickable tickable) {
        if (!world.isRemote) {
            TickDailyServer.addToQueue(() -> HFTrackers.getTickables(world).remove(tickable));
        }
    }

    @Override
    public void removeTickable(World world, BlockPos pos) {
        if (!world.isRemote) {
            TickDailyServer.addToQueue(() -> HFTrackers.getTickables(world).remove(pos));
        }
    }

    @Override
    public IDailyTickableBlock getTickableFromBlock(Block block) {
        if (block instanceof IDailyTickableBlock) {
            return ((IDailyTickableBlock)block);
        } else return registry.get(block);
    }
}
