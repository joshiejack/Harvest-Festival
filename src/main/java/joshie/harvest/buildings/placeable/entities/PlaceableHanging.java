package joshie.harvest.buildings.placeable.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.Validate;

import java.util.UUID;

public abstract class PlaceableHanging extends PlaceableEntity {
    private EnumFacing facing;

    public PlaceableHanging() {
        super(BlockPos.ORIGIN);
        this.facing = EnumFacing.NORTH;
    }

    public PlaceableHanging(EnumFacing facing, BlockPos offsetPos) {
        super(offsetPos);
        this.facing = facing;
    }

    @Override
    public Entity getEntity(UUID uuid, World world, BlockPos pos, Mirror mirror, Rotation rotation) {
        EntityHanging entity = getEntityHanging(uuid, world, pos, getFacing(mirror, rotation));
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

    public abstract EntityHanging getEntityHanging(UUID owner, World world, BlockPos pos, EnumFacing facing);

    public EnumFacing getFacing(Mirror mirror, Rotation rotation) {
        return rotation.rotate(mirror.mirror(this.facing));
    }
}