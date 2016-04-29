package joshie.harvest.buildings.placeable.entities;

import joshie.harvest.buildings.placeable.Placeable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.UUID;

public abstract class PlaceableEntity extends Placeable {
    public PlaceableEntity(BlockPos offsetPos) {
        super(offsetPos);
    }

    @Override
    public boolean canPlace(PlacementStage stage) {
        return stage == PlacementStage.ENTITIES;
    }

    public abstract Entity getEntity(UUID uuid, World world, BlockPos pos, boolean n1, boolean n2, boolean swap);

    public abstract String getStringFor(Entity entity, BlockPos pos);

    @Override
    public boolean place(UUID uuid, World world, BlockPos pos, IBlockState state, boolean n1, boolean n2, boolean swap) {
        return world.spawnEntityInWorld(getEntity(uuid, world, pos, n1, n2, swap));
    }
}