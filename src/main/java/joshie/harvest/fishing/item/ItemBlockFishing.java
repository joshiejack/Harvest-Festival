package joshie.harvest.fishing.item;

import joshie.harvest.core.base.item.ItemBlockHF;
import joshie.harvest.fishing.block.BlockFloating;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class ItemBlockFishing extends ItemBlockHF<BlockFloating> {
    public ItemBlockFishing(BlockFloating block) {
        super(block);
    }

    @Override
    @Nonnull
    @SuppressWarnings("ConstantConditions")
    public ActionResult<ItemStack> onItemRightClick(@Nonnull ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
        IBlockState state = getBlock().getStateFromMeta(stack.getItemDamage());
        RayTraceResult raytraceresult = rayTrace(world, player, true);
        if (raytraceresult == null) {
            return new ActionResult<>(EnumActionResult.PASS, stack);
        } else {
            if (raytraceresult.typeOfHit == RayTraceResult.Type.BLOCK) {
                BlockPos blockpos = raytraceresult.getBlockPos();
                if (!world.isBlockModifiable(player, blockpos) || !player.canPlayerEdit(blockpos.offset(raytraceresult.sideHit), raytraceresult.sideHit, stack)) {
                    return new ActionResult<>(EnumActionResult.FAIL, stack);
                }

                BlockPos blockpos1 = blockpos.up();
                IBlockState iblockstate = world.getBlockState(blockpos);
                if (iblockstate.getMaterial() == Material.WATER && (iblockstate.getValue(BlockLiquid.LEVEL)) == 0 && world.isAirBlock(blockpos1)) {
                    // special case for handling block placement with water lilies
                    net.minecraftforge.common.util.BlockSnapshot blocksnapshot = net.minecraftforge.common.util.BlockSnapshot.getBlockSnapshot(world, blockpos1);
                    world.setBlockState(blockpos1, state);
                    if (net.minecraftforge.event.ForgeEventFactory.onPlayerBlockPlace(player, blocksnapshot, net.minecraft.util.EnumFacing.UP).isCanceled()) {
                        blocksnapshot.restore(true, false);
                        return new ActionResult<>(EnumActionResult.FAIL, stack);
                    }

                    world.setBlockState(blockpos1, state, 11);

                    if (!player.capabilities.isCreativeMode) {
                        --stack.stackSize;
                    }

                    player.addStat(StatList.getObjectUseStats(this));
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
        return EnumActionResult.PASS;
    }
}
