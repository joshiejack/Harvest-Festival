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
        this.world = world;
        this.pos = pos;
    }

    @Override
    public void placeInPortal(Entity entity, float rotationYaw) {
        //world.getBlockState(pos);
        //entity.setPositionAndUpdate(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D);
        entity.motionX = 0.0f;
        entity.motionY = 0.0f;
        entity.motionZ = 0.0f;
    }
}
