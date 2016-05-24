package joshie.harvest.items;

import joshie.harvest.blocks.BlockGathering;
import joshie.harvest.core.HFTab;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class ItemAxe extends ItemBaseTool {
    public ItemAxe() {
        setCreativeTab(HFTab.GATHERING);
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.BOW;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer playerIn, EnumHand hand) {
        RayTraceResult result = getMovingObjectPositionFromPlayer(world, playerIn);
        if (result != null) {
            IBlockState state = world.getBlockState(result.getBlockPos());
            if (state.getBlock() instanceof BlockGathering) {
                return super.onItemRightClick(stack, world, playerIn, hand);
            }
        }

        return new ActionResult<>(EnumActionResult.PASS, stack);
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase entityLiving, int timeLeft) {
        int charge = (Math.min(7, Math.max(0, getCharge(stack) - 1)));
        setCharge(stack, 0); //Reset the charge

        RayTraceResult result = getMovingObjectPositionFromPlayer(world, entityLiving);
        if (result != null) {
            IBlockState state = world.getBlockState(result.getBlockPos());
            if (state.getBlock() instanceof BlockGathering) {
                ((BlockGathering) state.getBlock()).smashBlock(world, result.getBlockPos(), state, entityLiving, ToolTier.values()[charge]);
            }
        }
    }
}