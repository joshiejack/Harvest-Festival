package joshie.harvestmoon.buildings.placeable.entities;

import joshie.harvestmoon.buildings.placeable.Placeable;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public abstract class PlaceableEntity extends Placeable {    
    public PlaceableEntity(int offsetX, int offsetY, int offsetZ) {
        super(offsetX, offsetY, offsetZ);
    }
    
    public abstract Entity getEntity(World world, int x, int y, int z);    
    public abstract String getStringFor(Entity e, int x, int y, int z);
    
    
    @Override
    public void place(World world, int x, int y, int z, boolean n1, boolean n2, boolean swap) {
        world.spawnEntityInWorld(getEntity(world, x, y, z));
    }
}
