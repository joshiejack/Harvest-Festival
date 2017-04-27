package joshie.harvest.core.base.item;

import joshie.harvest.api.gathering.ISmashable;
import joshie.harvest.api.gathering.ISmashable.ToolType;
import joshie.harvest.core.helpers.EntityHelper;
import joshie.harvest.tools.ToolHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Set;

public abstract class ItemToolSmashing<I extends ItemToolSmashing> extends ItemTool<I> {
    public ItemToolSmashing(ToolTier tier, String toolClass, Set<Block> effective) {
        super(tier, toolClass, effective);
    }

    @Override
    @Nonnull
    public EnumAction getItemUseAction(@Nonnull ItemStack stack) {
        return EnumAction.NONE;
    }

    @Override
    public float getExhaustionRate(@Nonnull ItemStack stack) {
        ToolTier tier = getTier(stack);
        switch (tier) {
            case BASIC:
                return 3F;
            case COPPER:
                return 1.25F;
            case SILVER:
                return 0.333F;
            case GOLD:
                return 0.222F;
            case MYSTRIL:
                return 0.055F;
            case CURSED:
                return 25F;
            case BLESSED:
                return 0.02F;
            case MYTHIC:
                return 0.005F;
            default:
                return 1F;
        }
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

    public boolean onSmashed(EntityPlayer player, ItemStack stack, ToolTier tier, int harvestLevel, World world, BlockPos pos, IBlockState state) {
        if (canUse(stack)) {
            if (state.getBlock() instanceof ISmashable) {
                int requiredLevel = state.getBlock().getHarvestLevel(state);
                if (harvestLevel >= requiredLevel) {
                    ISmashable smashable = ((ISmashable) state.getBlock());
                    if (smashable.getToolType() == getToolType()) {
                        if (smashable.smashBlock(player, world, pos, state, tier)) {
                            ToolHelper.performTask(player, stack, this);
                            if (!world.isRemote) {
                                onBlockDestroyed(stack, world, state, pos, player);
                            }

                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    public void smashBlock(World world, EntityPlayer player, BlockPos position, ItemStack stack, boolean jump) {
        ToolTier tier = jump ? getTier(stack) : ToolTier.BASIC;
        int harvestLevel = getHarvestLevel(stack, toolClass, player, world.getBlockState(position));
        boolean smashed = false;
        EnumFacing front = EntityHelper.getFacingFromEntity(player);
        for (int x = getXMinus(tier, front, position.getX()); x <= getXPlus(tier, front, position.getX()); x++) {
            for (int z = getZMinus(tier, front, position.getZ()); z <= getZPlus(tier, front, position.getZ()); z++) {
                BlockPos pos = new BlockPos(x, position.getY(), z);
                IBlockState state = world.getBlockState(pos);
                if (onSmashed(player, stack, tier, harvestLevel, world, pos, state)) {
                    smashed = true;
                }
            }
        }

        if (smashed && jump) {
            world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, position.getX() + 0.5D, position.getY() + 0.5D, position.getZ() + 0.5D, 0, 0, 0);
            playSound(world, position);
        }
    }
}
