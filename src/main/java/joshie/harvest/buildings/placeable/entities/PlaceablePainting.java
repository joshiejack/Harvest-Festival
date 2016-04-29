package joshie.harvest.buildings.placeable.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.UUID;

public class PlaceablePainting extends PlaceableHanging {
    private String painting;

    //Registration Purposes
    public PlaceablePainting() {
        super(EnumFacing.DOWN, BlockPos.ORIGIN);
    }

    public PlaceablePainting(String painting, EnumFacing facing, BlockPos offsetPos) {
        super(facing, offsetPos);
        this.painting = painting;
    }

    public PlaceablePainting(String painting, EnumFacing facing, int offsetX, int offsetY, int offsetZ) {
        this(painting, facing, new BlockPos(offsetX, offsetY, offsetZ));
    }

    @Override
    public Entity getEntity(UUID uuid, World world, BlockPos pos, boolean n1, boolean n2, boolean swap) {
        EnumFacing facing = getFacing(n1, n2, swap);
        return new EntityPainting(world, new BlockPos(getX(pos.getX(), facing), pos.getY(), getZ(pos.getZ(), facing)), facing, painting);
    }

    @Override
    public String getStringFor(Entity e, BlockPos pos) {
        EntityPainting p = (EntityPainting) e;
        return "list.add(new PlaceablePainting(\"" + p.art.title + "\", " + p.getHangingPosition() + ", " + pos + "));";
    }
}