package joshie.harvest.fishing.item;

import joshie.harvest.core.base.item.ItemBlockHF;
import joshie.harvest.fishing.HFFishing;
import joshie.harvest.fishing.block.BlockFishing;
import joshie.harvest.fishing.block.BlockFishing.FishingBlock;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class ItemBlockFishing extends ItemBlockHF<BlockFishing> {
    public ItemBlockFishing(BlockFishing block) {
        super(block);
    }

    @Override
    @Nonnull
    @SuppressWarnings("ConstantConditions")
    public ActionResult<ItemStack> onItemRightClick(@Nonnull ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
        if (getBlock().getEnumFromStack(stack) != FishingBlock.HATCHERY) return new ActionResult<>(EnumActionResult.PASS, stack);
        RayTraceResult raytraceresult = rayTrace(world, player, true);
        if (raytraceresult == null) {
            return new ActionResult<>(EnumActionResult.PASS, stack);
        } else {
            if (raytraceresult.typeOfHit == RayTraceResult.Type.BLOCK) {
                BlockPos blockpos = raytraceresult.getBlockPos();
                if (!world.isBlockModifiable(player, blockpos) || !player.canPlayerEdit(blockpos.offset(raytraceresult.sideHit), raytraceresult.sideHit, stack)) {
                    return new ActionResult<>(EnumActionResult.FAIL, stack);
                }

                IBlockState iblockstate = world.getBlockState(blockpos);
                if (iblockstate.getMaterial() == Material.WATER && (iblockstate.getValue(BlockLiquid.LEVEL)) == 0) {
                    world.setBlockState(blockpos, HFFishing.FISHING_BLOCK.getStateFromEnum(FishingBlock.HATCHERY), 11);
                    if (!player.capabilities.isCreativeMode) {
                        --stack.stackSize;
                    }

                    world.playSound(player, blockpos, SoundEvents.BLOCK_WATERLILY_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    return new ActionResult<>(EnumActionResult.SUCCESS, stack);
                }
            }

            return new ActionResult<>(EnumActionResult.FAIL, stack);
        }
    }

    @Override
    @Nonnull
    public EnumActionResult onItemUse(@Nonnull ItemStack stack, @Nonnull EntityPlayer playerIn, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull EnumHand hand, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ) {
        FishingBlock fishing = getBlock().getEnumFromStack(stack);
        if (fishing == FishingBlock.HATCHERY) {
            return EnumActionResult.PASS;
        } else {
            if (worldIn.getBlockState(pos.up(2)).getMaterial() == Material.WATER) return super.onItemUse(stack, playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ);
            else return EnumActionResult.PASS;
        }
    }
}
