package joshie.harvest.mining;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class MiningTeleporter extends Teleporter {
    private final WorldServer world;
    private final BlockPos pos;

    public MiningTeleporter(WorldServer world, BlockPos pos) {
        super(world);
        this.pos = pos;
        this.world = world;
    }

    @Override
    public void placeInPortal(Entity entity, float rotationYaw) {
        entity.motionX = 0.0f;
        entity.motionY = 0.0f;
        entity.motionZ = 0.0f;
    }
}
