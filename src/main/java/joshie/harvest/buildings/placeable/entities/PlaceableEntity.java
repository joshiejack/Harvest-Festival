package joshie.harvest.buildings.placeable.entities;

import joshie.harvest.buildings.placeable.Placeable;
import net.minecraft.entity.Entity;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.UUID;

public abstract class PlaceableEntity extends Placeable {
    public PlaceableEntity(BlockPos offsetPos) {
        super(offsetPos);
    }

    @Override
    public boolean canPlace(ConstructionStage stage) {
        return stage == ConstructionStage.PAINT;
    }

    public abstract Entity getEntity(UUID uuid, World world, BlockPos pos, Mirror mirror, Rotation rotation);

    public abstract String getStringFor(Entity entity, BlockPos pos);

    @Override
    public boolean place (UUID owner, World world, BlockPos pos, Mirror mirror, Rotation rotation) {
        return world.spawnEntityInWorld(getEntity(owner, world, pos, mirror, rotation));
    }
}