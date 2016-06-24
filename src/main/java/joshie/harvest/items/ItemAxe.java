package joshie.harvest.items;

import joshie.harvest.blocks.BlockGathering;
import joshie.harvest.core.HFTab;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class ItemAxe extends ItemBaseTool {
    public ItemAxe() {
        setCreativeTab(HFTab.GATHERING);
    }

    @Override
    public void onFinishedCharging(World world, EntityLivingBase entity, @Nullable RayTraceResult result, ItemStack stack, ToolTier tier) {
        if (result != null && entity instanceof EntityPlayer) {
            IBlockState state = world.getBlockState(result.getBlockPos());
            if (state.getBlock() instanceof BlockGathering) {
                ((BlockGathering) state.getBlock()).smashBlock((EntityPlayer) entity, world, result.getBlockPos(), state, stack, tier);
            }
        }
    }
}