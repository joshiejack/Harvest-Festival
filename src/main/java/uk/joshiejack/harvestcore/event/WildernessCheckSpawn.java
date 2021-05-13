package uk.joshiejack.harvestcore.event;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.Event;

@Event.HasResult
public class WildernessCheckSpawn extends WorldEvent {
    private final BlockPos target;

    public WildernessCheckSpawn(World world, BlockPos target) {
        super(world);
        this.target = target;
    }

    public BlockPos getTarget() {
        return target;
    }
}
