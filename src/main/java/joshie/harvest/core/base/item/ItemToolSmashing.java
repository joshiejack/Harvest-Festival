package joshie.harvest.core.base.item;

import joshie.harvest.api.gathering.ISmashable;
import joshie.harvest.api.gathering.ISmashable.ToolType;
import joshie.harvest.tools.ToolHelper;
import joshie.harvest.core.helpers.generic.DirectionHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Set;

public abstract class ItemToolSmashing extends ItemTool<ItemToolSmashing> {
    public ItemToolSmashing(String toolClass, Set<Block> effective) {
        super(toolClass, effective);
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

    @Override
    protected int getXMinus(ToolTier tier, EnumFacing facing, int x) {
        return x - getFront(tier);
    }

    @Override
    protected int getXPlus(ToolTier tier, EnumFacing facing, int x) {
        return x + getFront(tier);
    }

    @Override
    protected int getZMinus(ToolTier tier, EnumFacing facing, int z) {
        return z - getFront(tier);
    }

    @Override
    protected int getZPlus(ToolTier tier, EnumFacing facing, int z) {
        return z + getFront(tier);
    }

    public abstract ToolType getToolType();
    public abstract void playSound(World world, BlockPos pos);

    public void smashBlock(World world, EntityPlayer player, BlockPos position, ItemStack stack, boolean jump) {
        ToolTier tier = jump ? getTier(stack) : ToolTier.BASIC;
        boolean smashed = false;
        EnumFacing front = DirectionHelper.getFacingFromEntity(player);
        for (int x = getXMinus(tier, front, position.getX()); x <= getXPlus(tier, front, position.getX()); x++) {
            for (int z = getZMinus(tier, front, position.getZ()); z <= getZPlus(tier, front, position.getZ()); z++) {
                BlockPos pos = new BlockPos(x, position.getY(), z);
                IBlockState state = world.getBlockState(pos);
                if (state.getBlock() instanceof ISmashable) {
                    ISmashable smashable = ((ISmashable) state.getBlock());
                    if (smashable.getToolType() == getToolType()) {
                        if (smashable.smashBlock(player, world, pos, state, tier)) {
                            if (!world.isRemote) {
                                ToolHelper.performTask(player, stack, getExhaustionRate(stack));
                                onBlockDestroyed(stack, world, state, pos, player);
                            }

                            smashed = true;
                        }
                    }
                }
            }
        }

        if (smashed && jump) {
            world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, position.getX() + 0.5D, position.getY() + 0.5D, position.getZ() + 0.5D, 0, 0, 0);
            playSound(world, position);
        }
    }
}
