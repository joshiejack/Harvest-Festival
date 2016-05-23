package joshie.harvest.items;

import joshie.harvest.api.HFApi;
import joshie.harvest.blocks.HFBlocks;
import joshie.harvest.core.helpers.PlayerHelper;
import joshie.harvest.core.helpers.generic.DirectionHelper;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemHoe extends ItemBaseTool {
    @Override
    public int getFront(ItemStack stack) {
        ToolTier tier = getTier(stack);
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
                return 11;
            case MYTHIC:
                return 17;
            default:
                return 0;
        }
    }

    @Override
    public int getSides(ItemStack stack) {
        ToolTier tier = getTier(stack);
        switch (tier) {
            case BASIC:
            case COPPER:
            case SILVER:
            case GOLD:
            case MYSTRIL:
                return 0;
            case CURSED:
            case BLESSED:
                return 1;
            case MYTHIC:
                return 2;
            default:
                return 0;
        }
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!player.canPlayerEdit(pos.offset(facing), facing, stack)) return EnumActionResult.FAIL;
        else {
            EnumFacing front = DirectionHelper.getFacingFromEntity(player);
            Block initial = world.getBlockState(pos).getBlock();
            if (!(world.isAirBlock(pos.up()) && (initial == Blocks.GRASS || initial == Blocks.DIRT || initial == HFBlocks.DIRT))) {
                return EnumActionResult.FAIL;
            }

            //Facing North, We Want East and West to be 1, left * this.left //TODO Move properly to 1.9
            EnumActionResult changed = EnumActionResult.FAIL;
            for (int x2 = getXMinus(stack, front, pos.getX()); x2 <= getXPlus(stack, front, pos.getX()); x2++) {
                for (int z2 = getZMinus(stack, front, pos.getZ()); z2 <= getZPlus(stack, front, pos.getZ()); z2++) {
                    Block block = world.getBlockState(new BlockPos(x2, pos.getY(), z2)).getBlock();
                    if (world.isAirBlock(pos.up())) {
                        changed = EnumActionResult.SUCCESS;
                        if ((block == Blocks.GRASS || block == Blocks.DIRT)) {
                            doParticles(stack, player, world, new BlockPos(x2, pos.getY(), z2));
                            if (!world.isRemote) {
                                world.setBlockState(new BlockPos(x2, pos.getY(), z2), HFBlocks.FARMLAND.getDefaultState());
                                HFApi.tickable.addTickable(world, new BlockPos(x2, pos.getY(), z2));
                            }
                        }
                    }
                }
            }

            return changed;
        }
    }

    private void doParticles(ItemStack stack, EntityPlayer player, World world, BlockPos pos) {
        displayParticle(world, pos, EnumParticleTypes.BLOCK_CRACK, Blocks.DIRT.getDefaultState());
        playSound(world, pos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS);
        PlayerHelper.performTask(player, stack, getExhaustionRate(stack));
    }
}