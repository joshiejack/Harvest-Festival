package joshie.harvest.core;

import joshie.harvest.api.ticking.IDailyTickable;
import joshie.harvest.api.ticking.IDailyTickableBlock;
import joshie.harvest.api.ticking.IDailyTickableRegistry;
import joshie.harvest.core.handlers.DailyTickHandler;
import joshie.harvest.core.util.annotations.HFApiImplementation;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;

@HFApiImplementation
public class HFDailyTickable implements IDailyTickableRegistry {
    public static final HFDailyTickable INSTANCE = new HFDailyTickable();
    private final HashMap<Block, IDailyTickableBlock> registry = new HashMap<>();

    private HFDailyTickable() {}

    @Override
    public void registerDailyTickableBlock(Block block, IDailyTickableBlock daily) {
        registry.put(block, daily);
    }

    @Override
    public void addTickable(World world, IDailyTickable tickable) {
        if (!world.isRemote) {
            DailyTickHandler.addToQueue(() -> HFTrackers.getTickables(world).add(tickable));
        }
    }

    @Override
    public void addTickable(World world, BlockPos pos, IDailyTickableBlock tickable) {
        if (!world.isRemote) {
            DailyTickHandler.addToQueue(() -> HFTrackers.getTickables(world).add(pos, tickable));
        }
    }

    @Override
    public void removeTickable(World world, IDailyTickable tickable) {
        if (!world.isRemote) {
            DailyTickHandler.addToQueue(() -> HFTrackers.getTickables(world).remove(tickable));
        }
    }

    @Override
    public void removeTickable(World world, BlockPos pos) {
        if (!world.isRemote) {
            DailyTickHandler.addToQueue(() -> HFTrackers.getTickables(world).remove(pos));
        }
    }

    @Override
    public IDailyTickableBlock getTickableFromBlock(Block block) {
        if (block instanceof IDailyTickableBlock) {
            return ((IDailyTickableBlock)block);
        } else return registry.get(block);
    }
}
