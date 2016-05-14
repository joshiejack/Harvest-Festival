package joshie.harvest.buildings.placeable.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PlaceablePainting extends PlaceableHanging {
    private String painting;

    @Override
    public EntityHanging getEntityHanging(World world, BlockPos pos, EnumFacing facing) {
        return new EntityPainting(world, pos, facing, painting);
    }

    @Override
    public String getStringFor(Entity e, BlockPos pos) {
        EntityPainting p = (EntityPainting) e;
        return "list.add(new PlaceablePainting(\"" + p.art.title + "\", " + "EnumFacing." + p.facingDirection.name().toUpperCase() +
                ", new BlockPos(" + pos.getX() + ", " + pos.getY() + "," + pos.getZ() + ")));";
    }
}