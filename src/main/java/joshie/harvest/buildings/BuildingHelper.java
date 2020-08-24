package joshie.harvest.buildings;

import joshie.harvest.api.buildings.Building;
import joshie.harvest.buildings.render.BuildingKey;
import joshie.harvest.core.helpers.ChatHelper;
import joshie.harvest.core.helpers.EntityHelper;
import joshie.harvest.core.helpers.TextHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;
import java.util.WeakHashMap;

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

    @Nullable
    public static RayTraceResult rayTrace(EntityPlayer player, double blockReachDistance, float partialTicks) {
        Vec3d vec3d = getPositionEyes(player, partialTicks);
        Vec3d vec3d1 = player.getLook(partialTicks);
        Vec3d vec3d2 = vec3d.addVector(vec3d1.x * blockReachDistance, vec3d1.y * blockReachDistance, vec3d1.z * blockReachDistance);
        RayTraceResult result = player.world.rayTraceBlocks(vec3d, vec3d2, false, false, true);
        return result;
    }

    @SuppressWarnings("all")
    public static BlockPos getBlockLookingAt(EntityPlayer player) {
        RayTraceResult raytrace = BuildingHelper.rayTrace(player, 128, 0F);
        return raytrace == null ? null : raytrace.getBlockPos();
    }

    private static final WeakHashMap<EntityPlayer, BuildingKey> POSITIONS_SERVER = new WeakHashMap<>();
    private static final WeakHashMap<EntityPlayer, ItemStack> STACKS_SERVER = new WeakHashMap<>();
    private static BuildingKey POSITION_CLIENT;
    @Nonnull
    private static ItemStack STACK_CLIENT = ItemStack.EMPTY;

    private static void validateOrInvalidateStack(@Nonnull ItemStack stack, EntityPlayer player) {
        if (player.world.isRemote) {
            if (STACK_CLIENT != stack) {
                STACK_CLIENT = stack;
                POSITION_CLIENT = null;
            }
        } else {
            if (STACKS_SERVER.get(player) != stack) {
                STACKS_SERVER.put(player, stack);
                POSITIONS_SERVER.remove(player);
            }
        }
    }

    private static void setPositionForPlayer(EntityPlayer player, BuildingKey pos) {
        if (player.world.isRemote) POSITION_CLIENT = pos;
        else POSITIONS_SERVER.put(player, pos);
    }

    private static BuildingKey getPositionForPlayer(EntityPlayer player) {
        if (player.world.isRemote) return POSITION_CLIENT;
        else return POSITIONS_SERVER.get(player);
    }

    private static boolean isAir(World world, BlockPos pos) {
        return world.isAirBlock(pos);
    }

    private static BuildingKey getCachedKey(EntityPlayer player, BlockPos pos, Building building) {
        EnumFacing facing = EntityHelper.getFacingFromEntity(player).getOpposite();
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
        if (facing == EnumFacing.NORTH) pos = pos.offset(facing, length).offset(EnumFacing.EAST, width);
        else if (facing == EnumFacing.SOUTH) pos = pos.offset(facing, length).offset(EnumFacing.WEST, width);
        else if (facing == EnumFacing.EAST) pos = pos.offset(facing, length).offset(EnumFacing.SOUTH, width);
        else if (facing == EnumFacing.WEST) pos = pos.offset(facing, length).offset(EnumFacing.NORTH, width);
        return BuildingKey.of(rotation, building, pos);
    }

    @SuppressWarnings("deprecation")
    public static BuildingKey getPositioning(@Nonnull ItemStack stack, World world, RayTraceResult raytrace, Building building, EntityPlayer player, boolean clicked) {
        validateOrInvalidateStack(stack, player);
        BlockPos pos = raytrace.getBlockPos().offset(raytrace.sideHit).up(building.getOffsetY());
        //Load the saved position
        BuildingKey key = getPositionForPlayer(player);
        if (key == null) {
            if (clicked) {
                if (!isAir(world, raytrace.getBlockPos()) && raytrace.sideHit == EnumFacing.UP) {
                    setPositionForPlayer(player, getCachedKey(player, pos, building));
                    if (world.isRemote) {
                        ChatHelper.displayChat(TextFormatting.YELLOW + building.getLocalisedName() + TextFormatting.RESET + " " + TextHelper.translate("town.preview") + "\n-"
                                + TextHelper.translate("town.sneak") + " " + TextFormatting.GREEN + "" + TextHelper.translate("town.confirm") + "\n-"
                                + TextFormatting.RESET + TextHelper.translate("town.click") + " " + TextFormatting.RED + TextHelper.translate("town.cancel"));
                    }
                }

                return null;
            }
        } else {
            if (clicked) {
                if (!player.isSneaking() && world.isRemote) {
                    ChatHelper.displayChat(TextFormatting.RED + building.getLocalisedName() + " " + TextHelper.translate("town.cancelled"));
                }

                setPositionForPlayer(player, null);
                return player.isSneaking() ? key : null;
            }
        }

        return key == null ? raytrace.sideHit != EnumFacing.UP ? null : getCachedKey(player, pos, building) : key;
    }

    public static ActionResult<ItemStack> displayErrors(World world, @Nonnull ItemStack stack, List<BuildingError> errors) {
        if (world.isRemote) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < errors.size(); i++) {
                String string = TextFormatting.RED + TextHelper.translate("town.failure") + " " + TextFormatting.WHITE + TextHelper.translate("town." + errors.get(i).name().toLowerCase(Locale.ENGLISH));
                builder.append(string);
                if (i < errors.size() + 1) builder.append("\n");
            }

            ChatHelper.displayChat(builder.toString());
        }

        return new ActionResult<>(EnumActionResult.PASS, stack);
    }
}