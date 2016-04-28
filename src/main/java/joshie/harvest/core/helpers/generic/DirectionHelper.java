package joshie.harvest.core.helpers.generic;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;

public class DirectionHelper {
    public static EnumFacing getFacingFromEntity(EntityLivingBase entity) {
        int facing = MathHelper.floor_double(entity.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
        EnumFacing dir = EnumFacing.NORTH;
        if (facing == 0) return EnumFacing.NORTH;
        if (facing == 1) return EnumFacing.EAST;
        if (facing == 2) return EnumFacing.SOUTH;
        if (facing == 3) return EnumFacing.WEST;
        return dir;
    }
}