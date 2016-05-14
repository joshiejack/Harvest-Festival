package joshie.harvest.buildings.placeable.entities;

import joshie.harvest.blocks.BlockPreview.Direction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.Validate;

public abstract class PlaceableHanging extends PlaceableEntity {
    private EnumFacing facing;

    @Override
    public Entity getEntity(World world, BlockPos pos, Direction direction) {
        EntityHanging entity = getEntityHanging(world, pos, getFacing(direction.getMirror(), direction.getRotation()));
        if (!entity.onValidSurface()) {
            EnumFacing opposite = entity.facingDirection.getOpposite();
            Validate.notNull(opposite);
            Validate.isTrue(opposite.getAxis().isHorizontal());
            entity.facingDirection = opposite;
            entity.prevRotationYaw = entity.rotationYaw = (float)(entity.facingDirection.getHorizontalIndex() * 90);
            entity.setPosition(entity.posX, entity.posY, entity.posZ);
        }

        return entity;
    }

    public abstract EntityHanging getEntityHanging(World world, BlockPos pos, EnumFacing facing);

    public EnumFacing getFacing(Mirror mirror, Rotation rotation) {
        return rotation.rotate(mirror.mirror(this.facing));
    }
}