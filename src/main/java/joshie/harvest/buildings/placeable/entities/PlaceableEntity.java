package joshie.harvest.buildings.placeable.entities;

import java.util.UUID;

import joshie.harvest.buildings.placeable.Placeable;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public abstract class PlaceableEntity extends Placeable {    
    public PlaceableEntity(int offsetX, int offsetY, int offsetZ) {
        super(offsetX, offsetY, offsetZ);
    }
    
    @Override
    public boolean canPlace(PlacementStage stage) {
        return stage == PlacementStage.ENTITIES;
    }
    
    public abstract Entity getEntity(UUID uuid, World world, int x, int y, int z, boolean n1, boolean n2, boolean swap);    
    public abstract String getStringFor(Entity e, int x, int y, int z);
    
    
    @Override
    public boolean place(UUID uuid, World world, int x, int y, int z, boolean n1, boolean n2, boolean swap) {
        return world.spawnEntityInWorld(getEntity(uuid, world, x, y, z, n1, n2, swap));
    }
}
