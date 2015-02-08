package joshie.harvestmoon.buildings.placeable.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.world.World;

public class PlaceablePainting extends PlaceableEntity {
    private String painting;
    private int facing;

    //Registration Purposes
    public PlaceablePainting(){
        super(0, 0, 0);
    }
    
    public PlaceablePainting(String painting, int facing, int offsetX, int offsetY, int offsetZ) {
        super(offsetX, offsetY, offsetZ);
        this.painting = painting;
        this.facing = facing;
    }

    @Override
    public Entity getEntity(World world, int x, int y, int z) {
        return new EntityPainting(world, x, y, z, facing, painting);
    }

    @Override
    public String getStringFor(Entity e, int x, int y, int z) {
        EntityPainting p = (EntityPainting) e;
        return "list.add(new DataPainting(\"" + p.art.title + "\", " + p.hangingDirection + ", " + x + ", " + y + ", " + z + "));";
    }
}
