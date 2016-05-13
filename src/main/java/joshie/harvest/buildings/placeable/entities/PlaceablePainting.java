package joshie.harvest.buildings.placeable.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PlaceablePainting extends PlaceableHanging {
    private String painting;

    //Registration Purposes
    public PlaceablePainting() {
        super(EnumFacing.SOUTH, BlockPos.ORIGIN);
    }

    public PlaceablePainting(String painting, EnumFacing facing, BlockPos offsetPos) {
        super(facing, offsetPos);
        this.painting = painting;
    }

    public PlaceablePainting(String painting, EnumFacing facing, int offsetX, int offsetY, int offsetZ) {
        this(painting, facing, new BlockPos(offsetX, offsetY, offsetZ));
    }

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