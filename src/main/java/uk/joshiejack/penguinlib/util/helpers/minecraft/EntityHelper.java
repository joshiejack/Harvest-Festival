package uk.joshiejack.penguinlib.util.helpers.minecraft;

import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.List;

public class EntityHelper {
    public static <T extends Entity> List<T> getEntities(Class<? extends T> t, World world, BlockPos pos, double size, double ySize) {
        return world.getEntitiesWithinAABB(t, new AxisAlignedBB(pos.getX() - 0.5F, pos.getY() - 0.5F, pos.getZ() - 0.5F, pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F).grow(size, ySize, size));
    }

    public static EnumFacing getFacingFromEntity(Entity entity) {
        int facing = MathHelper.floor(entity.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
        return facing == 0 ? EnumFacing.NORTH : facing == 1 ? EnumFacing.EAST : facing == 2 ? EnumFacing.SOUTH : EnumFacing.WEST;
    }

    public static boolean forbidsEntityDrops(Block block) {
        return block instanceof BlockDoor || block instanceof BlockFenceGate || block instanceof BlockTrapDoor || block instanceof BlockLever || block instanceof BlockButton || block instanceof BlockChest;
    }

    public static Rotation getRotationFromEntity(EntityLivingBase entity) {
        EnumFacing facing = getFacingFromEntity(entity).getOpposite();
        switch (facing) {
            case NORTH:
                return Rotation.CLOCKWISE_90;
            case SOUTH:
                return Rotation.COUNTERCLOCKWISE_90;
            case EAST:
                return Rotation.CLOCKWISE_180;
            case WEST:
                return Rotation.NONE;
        }

        return Rotation.NONE;
    }
}
