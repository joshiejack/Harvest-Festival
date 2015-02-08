package joshie.harvestmoon.buildings.placeable.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.world.World;

public class PlaceablePainting extends PlaceableHanging {
    private String painting;
    private int facing;

    //Registration Purposes
    public PlaceablePainting() {
        super(0, 0, 0, 0);
    }

    public PlaceablePainting(String painting, int facing, int offsetX, int offsetY, int offsetZ) {
        super(facing, offsetX, offsetY, offsetZ);
        this.painting = painting;
    }

    @Override
    public Entity getEntity(World world, int x, int y, int z, boolean n1, boolean n2, boolean swap) {
        int facing = getFacing(n1, n2, swap);
        return new EntityPainting(world, getX(x, facing), y, getZ(z, facing), facing, painting);
    }

    @Override
    public String getStringFor(Entity e, int x, int y, int z) {
        EntityPainting p = (EntityPainting) e;
        return "list.add(new PlaceablePainting(\"" + p.art.title + "\", " + p.hangingDirection + ", " + x + ", " + y + ", " + z + "));";
    }
}
