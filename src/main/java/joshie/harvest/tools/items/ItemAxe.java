package joshie.harvest.tools.items;

import com.google.common.collect.Sets;
import joshie.harvest.api.gathering.ISmashable;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.base.ItemBaseTool;
import joshie.harvest.core.helpers.ToolHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import javax.annotation.Nullable;

import java.util.Set;

import static joshie.harvest.api.gathering.ISmashable.ToolType.AXE;

public class ItemAxe extends ItemBaseTool {
    private static final Set<Block> EFFECTIVE_ON = Sets.newHashSet(Blocks.PLANKS, Blocks.BOOKSHELF, Blocks.LOG, Blocks.LOG2, Blocks.CHEST, Blocks.PUMPKIN, Blocks.LIT_PUMPKIN, Blocks.MELON_BLOCK, Blocks.LADDER, Blocks.WOODEN_BUTTON, Blocks.WOODEN_PRESSURE_PLATE);

    public ItemAxe() {
        super("axe", EFFECTIVE_ON);
        setCreativeTab(HFTab.GATHERING);
    }

    @Override
    public float getStrVsBlock(ItemStack stack, IBlockState state) {
        Material material = state.getMaterial();
        return material != Material.WOOD && material != Material.PLANTS && material != Material.VINE ? super.getStrVsBlock(stack, state) : this.getEffiency(stack);
    }

    @Override
    public void onFinishedCharging(World world, EntityLivingBase entity, @Nullable RayTraceResult result, ItemStack stack, ToolTier tier) {
        if (result != null && entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer)entity;
            IBlockState state = world.getBlockState(result.getBlockPos());
            if (state.getBlock() instanceof ISmashable) {
                ISmashable smashable = ((ISmashable)state.getBlock());
                if (smashable.getToolType() == AXE) {
                    if(smashable.smashBlock(player, world, result.getBlockPos(), state, tier)) {
                        ToolHelper.performTask(player, stack, getExhaustionRate(stack));
                        onBlockDestroyed(stack, world, state, result.getBlockPos(), entity);
                    }
                }
            }
        }
    }
}