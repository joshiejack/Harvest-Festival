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

import static joshie.harvest.api.gathering.ISmashable.ToolType.HAMMER;

public class ItemHammer extends ItemBaseTool {
    private static final Set<Block> EFFECTIVE_ON = Sets.newHashSet(Blocks.ACTIVATOR_RAIL, Blocks.COAL_ORE, Blocks.COBBLESTONE, Blocks.DETECTOR_RAIL, Blocks.DIAMOND_BLOCK, Blocks.DIAMOND_ORE, Blocks.DOUBLE_STONE_SLAB, Blocks.GOLDEN_RAIL, Blocks.GOLD_BLOCK, Blocks.GOLD_ORE, Blocks.ICE, Blocks.IRON_BLOCK, Blocks.IRON_ORE, Blocks.LAPIS_BLOCK, Blocks.LAPIS_ORE, Blocks.LIT_REDSTONE_ORE, Blocks.MOSSY_COBBLESTONE, Blocks.NETHERRACK, Blocks.PACKED_ICE, Blocks.RAIL, Blocks.REDSTONE_ORE, Blocks.SANDSTONE, Blocks.RED_SANDSTONE, Blocks.STONE, Blocks.STONE_SLAB, Blocks.STONE_BUTTON, Blocks.STONE_PRESSURE_PLATE);

    public ItemHammer() {
        super("pickaxe", EFFECTIVE_ON);
        setCreativeTab(HFTab.MINING);
    }

    @Override
    public float getStrVsBlock(ItemStack stack, IBlockState state) {
        Material material = state.getMaterial();
        return material != Material.IRON && material != Material.ANVIL && material != Material.ROCK ? super.getStrVsBlock(stack, state) : this.getEffiency(stack);
    }

    @Override
    public void onFinishedCharging(World world, EntityLivingBase entity, @Nullable RayTraceResult result, ItemStack stack, ToolTier tier) {
        if (result != null && entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
            IBlockState state = world.getBlockState(result.getBlockPos());
            if (state.getBlock() instanceof ISmashable) {
                ISmashable smashable = ((ISmashable) state.getBlock());
                if (smashable.getToolType() == HAMMER) {
                    if (smashable.smashBlock(player, world, result.getBlockPos(), state, tier)) {
                        ToolHelper.performTask(player, stack, getExhaustionRate(stack));
                        onBlockDestroyed(stack, world, state, result.getBlockPos(), entity);
                    }
                }
            }
        }
    }
}