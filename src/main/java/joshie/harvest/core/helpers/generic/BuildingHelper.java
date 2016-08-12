package joshie.harvest.core.helpers.generic;

import joshie.harvest.buildings.Building;
import joshie.harvest.buildings.render.BuildingKey;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class BuildingHelper {
    public static Vec3d getPositionEyes(EntityPlayer player, float partialTicks) {
        if (partialTicks == 1.0F) {
            return new Vec3d(player.posX, player.posY + (double) player.getEyeHeight(), player.posZ);
        } else {
            double d0 = player.prevPosX + (player.posX - player.prevPosX) * (double) partialTicks;
            double d1 = player.prevPosY + (player.posY - player.prevPosY) * (double) partialTicks + (double) player.getEyeHeight();
            double d2 = player.prevPosZ + (player.posZ - player.prevPosZ) * (double) partialTicks;
            return new Vec3d(d0, d1, d2);
        }
    }

    public static RayTraceResult rayTrace(EntityPlayer player, double blockReachDistance, float partialTicks) {
        Vec3d vec3d = getPositionEyes(player, partialTicks);
        Vec3d vec3d1 = player.getLook(partialTicks);
        Vec3d vec3d2 = vec3d.addVector(vec3d1.xCoord * blockReachDistance, vec3d1.yCoord * blockReachDistance, vec3d1.zCoord * blockReachDistance);
        return player.worldObj.rayTraceBlocks(vec3d, vec3d2, false, false, true);
    }

    @Nonnull
    public static BuildingKey getPositioning(World world, RayTraceResult raytrace, Building building, EntityPlayer player, EnumHand hand) {
        BlockPos cachedBlock = raytrace.getBlockPos().offset(raytrace.sideHit);
        cachedBlock = cachedBlock.up(building.getOffsetY());
        IBlockState state = world.getBlockState(raytrace.getBlockPos());
        if (state.getBlock().getMaterial(state) == Material.AIR) {
            return null;
        }

        EnumFacing facing = DirectionHelper.getFacingFromEntity(player).getOpposite();
        Mirror mirror = hand == EnumHand.OFF_HAND ? player.isSneaking() ? Mirror.FRONT_BACK : Mirror.LEFT_RIGHT : Mirror.NONE;
        Rotation rotation = Rotation.NONE;
        if (facing == EnumFacing.NORTH) {
            rotation = Rotation.CLOCKWISE_90;
        } else if (facing == EnumFacing.SOUTH) {
            rotation = Rotation.COUNTERCLOCKWISE_90;
        } else if (facing == EnumFacing.EAST) {
            rotation = Rotation.CLOCKWISE_180;
        } else if (facing == EnumFacing.WEST) {
            rotation = Rotation.NONE;
        }

        int length = building.getLength();
        int width = building.getWidth();
        if (facing == EnumFacing.NORTH) cachedBlock = cachedBlock.offset(facing, length).offset(EnumFacing.EAST, width);
        else if (facing == EnumFacing.SOUTH) cachedBlock = cachedBlock.offset(facing, length).offset(EnumFacing.WEST, width);
        else if (facing == EnumFacing.EAST) cachedBlock = cachedBlock.offset(facing, length).offset(EnumFacing.SOUTH, width);
        else if (facing == EnumFacing.WEST) cachedBlock = cachedBlock.offset(facing, length).offset(EnumFacing.NORTH, width);
        return BuildingKey.of(cachedBlock, mirror, rotation, building);
    }
}
