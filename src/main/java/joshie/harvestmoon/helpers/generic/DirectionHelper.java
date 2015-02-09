package joshie.harvestmoon.helpers.generic;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.util.ForgeDirection;

public class DirectionHelper {
    public static ForgeDirection getFacingFromEntity(EntityLivingBase entity) {
        int facing = MathHelper.floor_double(entity.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
        ForgeDirection dir = ForgeDirection.NORTH;
        if (facing == 0) return ForgeDirection.NORTH;
        if (facing == 1) return ForgeDirection.EAST;
        if (facing == 2) return ForgeDirection.SOUTH;
        if (facing == 3) return ForgeDirection.WEST;
        return dir;
    }
}
