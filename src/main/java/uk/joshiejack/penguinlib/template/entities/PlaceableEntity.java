package uk.joshiejack.penguinlib.template.entities;

import uk.joshiejack.penguinlib.template.Placeable;
import net.minecraft.entity.Entity;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class PlaceableEntity extends Placeable {
    public PlaceableEntity() {}
    public PlaceableEntity(BlockPos position) {
        this.pos = position;
    }

    @Override
    public boolean canPlace(ConstructionStage stage) {
        return stage == ConstructionStage.PAINT;
    }

    public abstract Entity getEntity(World world, BlockPos pos, Rotation rotation);

    @Override
    protected boolean place (World world, BlockPos pos, Rotation rotation, boolean playSound) {
        Entity entity = getEntity(world, pos, rotation);
        return entity != null && world.spawnEntity(entity);
    }

    public abstract PlaceableEntity getCopyFromEntity(Entity entity, BlockPos position);

    public abstract Class<? extends Entity> getEntityClass();
}