package joshie.harvest.core;

import joshie.harvest.api.ticking.DailyTickableBlock;
import joshie.harvest.api.ticking.IDailyTickableRegistry;
import joshie.harvest.core.handlers.DailyTickHandler;
import joshie.harvest.core.util.annotations.HFApiImplementation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@HFApiImplementation
@SuppressWarnings("unused, WeakerAccess")
public class HFDailyTickable implements IDailyTickableRegistry {
    public static final HFDailyTickable INSTANCE = new HFDailyTickable();

    private HFDailyTickable() {}

    @Override
    public void addTickable(World world, BlockPos pos, DailyTickableBlock tickable) {
        if (!world.isRemote) DailyTickHandler.addToQueue(() -> HFTrackers.getTickables(world).add(pos, tickable));
    }
}
