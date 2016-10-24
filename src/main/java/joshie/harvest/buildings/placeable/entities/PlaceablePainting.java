package joshie.harvest.buildings.placeable.entities;

import com.google.gson.annotations.Expose;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PlaceablePainting extends PlaceableHanging {
    @Expose
    private String painting;

    public PlaceablePainting() {}
    public PlaceablePainting(String name, EnumFacing facing, int x, int y, int z) {
        super(facing, x, y, z);
        this.painting = name;
    }

    @Override
    public EntityHanging getEntityHanging(World world, BlockPos pos, EnumFacing facing) {
        EntityPainting painting = new EntityPainting(world, pos, facing);
        for (EntityPainting.EnumArt entitypainting$enumart : EntityPainting.EnumArt.values()) {
            if (entitypainting$enumart.title.equals(this.painting)) {
                painting.art = entitypainting$enumart;
                break;
            }
        }

        painting.updateFacingWithBoundingBox(facing);
        return painting;
    }

    @Override
    public PlaceablePainting getCopyFromEntity(Entity e, int x, int y, int z) {
        EntityPainting p = (EntityPainting) e;

        return new PlaceablePainting(p.art.title, p.facingDirection, x, y, z);
    }
}