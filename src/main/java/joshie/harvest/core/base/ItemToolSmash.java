package joshie.harvest.core.base;

import joshie.harvest.api.gathering.ISmashable;
import joshie.harvest.api.gathering.ISmashable.ToolType;
import joshie.harvest.core.helpers.ToolHelper;
import joshie.harvest.core.helpers.generic.DirectionHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Set;

public abstract class ItemToolSmash extends ItemTool {
    public ItemToolSmash(String toolClass, Set<Block> effective) {
        super(toolClass, effective);
    }

    @Override
    public int getSides(ToolTier tier) {
        return getFront(tier);
    }

    @Override
    public int getFront(ToolTier tier) {
        switch (tier) {
            case BASIC:
                return 0;
            case COPPER:
                return 1;
            case SILVER:
                return 2;
            case GOLD:
                return 3;
            case MYSTRIL:
                return 5;
            case CURSED:
            case BLESSED:
                return 7;
            case MYTHIC:
                return 15;
            default:
                return 0;
        }
    }

    public abstract ToolType getToolType();

    @Override
    public void onFinishedCharging(World world, EntityLivingBase entity, @Nullable RayTraceResult result, ItemStack stack, ToolTier tier) {
        if (result != null && entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer)entity;
            BlockPos position = result.getBlockPos();
            EnumFacing front = DirectionHelper.getFacingFromEntity(player);
            for (int x = getXMinus(tier, front, position.getX()); x <= getXPlus(tier, front, position.getX()); x++) {
                for (int z = getZMinus(tier, front, position.getZ()); z <= getZPlus(tier, front, position.getZ()); z++) {
                    BlockPos pos = new BlockPos(x, position.getY(), z);
                    IBlockState state = world.getBlockState(pos);
                    if (state.getBlock() instanceof ISmashable) {
                        ISmashable smashable = ((ISmashable) state.getBlock());
                        if (smashable.getToolType() == getToolType()) {
                            if (smashable.smashBlock(player, world, pos, state, tier)) {
                                ToolHelper.performTask(player, stack, getExhaustionRate(stack));
                                onBlockDestroyed(stack, world, state, pos, entity);
                            }
                        }
                    }
                }
            }
        }
    }
}
