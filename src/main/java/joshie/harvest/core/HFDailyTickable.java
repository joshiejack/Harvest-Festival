package joshie.harvest.core;

import joshie.harvest.api.core.IDailyTickable;
import joshie.harvest.api.core.IDailyTickableRegistry;
import joshie.harvest.core.handlers.HFTrackers;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class HFDailyTickable implements IDailyTickableRegistry {
    @Override
    public void addTickable(World world, IDailyTickable tickable) {
        if (!world.isRemote) {
            HFTrackers.getTickables(world).add(tickable);
        }
    }

    @Override
    public void addTickable(World world, BlockPos pos) {
        if (!world.isRemote) {
            HFTrackers.getTickables(world).add(pos);
        }
    }

    @Override
    public void removeTickable(World world, IDailyTickable tickable) {
        if (!world.isRemote) {
            HFTrackers.getTickables(world).remove(tickable);
        }
    }

    @Override
    public void removeTickable(World world, BlockPos pos) {
        if (!world.isRemote) {
            HFTrackers.getTickables(world).remove(pos);
        }
    }
}
