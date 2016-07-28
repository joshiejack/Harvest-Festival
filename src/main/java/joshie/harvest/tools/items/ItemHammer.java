package joshie.harvest.tools.items;

import joshie.harvest.api.gathering.ISmashable;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.helpers.PlayerHelper;
import joshie.harvest.core.util.base.ItemBaseTool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import javax.annotation.Nullable;

import static joshie.harvest.api.gathering.ISmashable.ToolType.HAMMER;

public class ItemHammer extends ItemBaseTool {
    public ItemHammer() {
        setCreativeTab(HFTab.MINING);
    }

    @Override
    public void onFinishedCharging(World world, EntityLivingBase entity, @Nullable RayTraceResult result, ItemStack stack, ToolTier tier) {
        if (result != null && entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer)entity;
            IBlockState state = world.getBlockState(result.getBlockPos());
            if (state.getBlock() instanceof ISmashable) {
                ISmashable smashable = ((ISmashable)state.getBlock());
                if (smashable.getToolType() == HAMMER) {
                    if(smashable.smashBlock(player, world, result.getBlockPos(), state, tier)) {
                        PlayerHelper.performTask(player, stack, getExhaustionRate(stack));
                    }
                }
            }
        }
    }
}