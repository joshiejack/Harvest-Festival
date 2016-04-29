package joshie.harvest.items;

import joshie.harvest.api.crops.IBreakCrops;
import joshie.harvest.blocks.BlockCrop;
import joshie.harvest.core.helpers.PlayerHelper;
import joshie.harvest.core.helpers.generic.DirectionHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemSickle extends ItemBaseTool implements IBreakCrops {
    @Override
    public int getFront(ItemStack stack) {
        ToolTier tier = getTier(stack);
        switch (tier) {
            case BASIC:
            case COPPER:
                return 0;
            case SILVER:
                return 1;
            case GOLD:
                return 2;
            case MYSTRIL:
                return 4;
            case CURSED:
            case BLESSED:
                return 8;
            case MYTHIC:
                return 14;
            default:
                return 0;
        }
    }

    @Override
    public int getSides(ItemStack stack) {
        ToolTier tier = getTier(stack);
        switch (tier) {
            case BASIC:
                return 0;
            case COPPER:
            case SILVER:
            case GOLD:
                return 1;
            case MYSTRIL:
                return 2;
            case CURSED:
            case BLESSED:
                return 4;
            case MYTHIC:
                return 7;
            default:
                return 0;
        }
    }

    @Override
    public float getStrVsBlock(ItemStack stack, IBlockState state) {
        Material material = state.getMaterial();
        return (state.getBlock() != Blocks.GRASS && material == Material.GRASS) || material == Material.LEAVES || material == Material.VINE ? 10F : getStrVsBlock(stack, state);
    }

    @Override
    public float getStrengthVSCrops(EntityPlayer player, World world, BlockPos pos, IBlockState state, ItemStack stack) {
        if (!player.canPlayerEdit(pos, EnumFacing.DOWN, stack)) return 0F;
        else {
            EnumFacing front = DirectionHelper.getFacingFromEntity(player);
            Block initial = world.getBlockState(pos).getBlock();
            if (!(initial instanceof BlockCrop)) {
                return 0F;
            }

            //Facing North, We Want East and West to be 1, left * this.left
            for (int x2 = getXMinus(stack, front, pos.getX()); x2 <= getXPlus(stack, front, pos.getX()); x2++) {
                for (int z2 = getZMinus(stack, front, pos.getZ()); z2 <= getZPlus(stack, front, pos.getZ()); z2++) {
                    Block block = world.getBlockState(new BlockPos(x2, pos.getY(), z2)).getBlock();
                    if (block instanceof BlockCrop) {
                        if (!world.isRemote) {
                            block.removedByPlayer(state, world, pos.add(x2, pos.getY(), z2), player, true);
                        }

                        displayParticle(world, pos.add(x2, pos.getY(), z2), EnumParticleTypes.BLOCK_CRACK);
                        playSound(world, pos.add(x2, pos.getY(), z2), SoundEvents.BLOCK_GRASS_BREAK, SoundCategory.BLOCKS);
                        PlayerHelper.performTask(player, stack, getExhaustionRate(stack));
                    }
                }
            }
        }
        return 1F;
    }
}