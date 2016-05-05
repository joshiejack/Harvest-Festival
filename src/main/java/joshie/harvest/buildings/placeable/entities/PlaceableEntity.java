package joshie.harvest.buildings.placeable.entities;

import joshie.harvest.blocks.BlockPreview.Direction;
import joshie.harvest.buildings.placeable.Placeable;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.UUID;

public abstract class PlaceableEntity extends Placeable {
    public PlaceableEntity(BlockPos offsetPos) {
        super(offsetPos.getX(), offsetPos.getY(), offsetPos.getZ());
    }

    @Override
    public boolean canPlace(ConstructionStage stage) {
        return stage == ConstructionStage.PAINT;
    }

    public abstract Entity getEntity(UUID uuid, World world, BlockPos pos, Direction direction);

    public abstract String getStringFor(Entity entity, BlockPos pos);

    @Override
    public boolean place (UUID owner, World world, BlockPos pos, Direction direction) {
        Entity entity = getEntity(owner, world, pos, direction);
        if (entity == null) return false;
        return world.spawnEntityInWorld(entity);
    }
}