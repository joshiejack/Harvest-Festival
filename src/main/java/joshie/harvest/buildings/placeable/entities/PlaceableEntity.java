package joshie.harvest.buildings.placeable.entities;

import joshie.harvest.blocks.BlockPreview.Direction;
import joshie.harvest.buildings.placeable.Placeable;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class PlaceableEntity extends Placeable {
    public PlaceableEntity() {}
    public PlaceableEntity(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public boolean canPlace(ConstructionStage stage) {
        return stage == ConstructionStage.PAINT;
    }

    public abstract Entity getEntity(World world, BlockPos pos, Direction direction);

    @Override
    public boolean place (World world, BlockPos pos, Direction direction) {
        Entity entity = getEntity(world, pos, direction);
        if (entity == null) return false;
        return world.spawnEntityInWorld(entity);
    }

    public abstract PlaceableEntity getCopyFromEntity(Entity entity, int x, int y, int z);
}