package joshie.harvest.core.util;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

import javax.annotation.Nonnull;

public class HFTeleporter extends Teleporter {
    public HFTeleporter(WorldServer world, BlockPos pos) {
        super(world);
    }

    @Override
    public void placeInPortal(@Nonnull Entity entity, float rotationYaw) {
        entity.motionX = 0.0f;
        entity.motionY = 0.0f;
        entity.motionZ = 0.0f;
    }
}
