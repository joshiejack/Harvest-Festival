package joshie.harvestmoon.buildings.data;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.world.World;

public class DataPainting extends EntityData {
    private String painting;
    private int facing;

    public DataPainting(){}
    public DataPainting(String painting, int facing) {
        this.painting = painting;
        this.facing = facing;
    }

    @Override
    public Entity getEntity(World world, int x, int y, int z) {
        return new EntityPainting(world, x, y, z, facing, painting);
    }

    @Override
    public String getStringFor(Entity e) {
        EntityPainting p = (EntityPainting) e;
        return "tempArray.add(new DataPainting(\"" + p.art.title + "\", " + p.hangingDirection + "));";
    }
}
