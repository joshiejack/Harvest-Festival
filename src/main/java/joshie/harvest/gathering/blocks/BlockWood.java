package joshie.harvest.gathering.blocks;

import joshie.harvest.api.core.ITiered.ToolTier;
import joshie.harvest.api.gathering.ISmashable;
import joshie.harvest.gathering.blocks.BlockWood.Wood;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.lib.CreativeSort;
import joshie.harvest.core.base.BlockHFEnum;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import static joshie.harvest.api.core.ITiered.ToolTier.*;
import static joshie.harvest.api.gathering.ISmashable.ToolType.AXE;

public class BlockWood extends BlockHFEnum<BlockWood, Wood> implements ISmashable {
    private static final AxisAlignedBB BRANCH_SMALL_AABB = new AxisAlignedBB(0.15D, 0.0D, 0.15D, 0.85D, 0.15D, 0.85D);
    private static final AxisAlignedBB BRANCH_MEDIUM_AABB = new AxisAlignedBB(0.15D, 0.0D, 0.15D, 0.85D, 0.25D, 0.85D);
    private static final AxisAlignedBB BRANCH_LARGE_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.9D, 1.0D);
    private static final AxisAlignedBB STUMP_SMALL_AABB = new AxisAlignedBB(0.2D, 0.0D, 0.2D, 0.8D, 0.25D, 0.8D);
    private static final AxisAlignedBB STUMP_MEDIUM_AABB = new AxisAlignedBB(0.1D, 0.0D, 0.1D, 0.9D, 0.35D, 0.9D);
    private static final AxisAlignedBB STUMP_LARGE_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);

    public enum Wood implements IStringSerializable {
        BRANCH_SMALL, BRANCH_MEDIUM, BRANCH_LARGE, STUMP_SMALL, STUMP_MEDIUM, STUMP_LARGE;

        @Override
        public String getName() {
            return toString().toLowerCase();
        }
    }

    public BlockWood() {
        super(Material.WOOD, Wood.class, HFTab.GATHERING);
        setHardness(0.5F);
        setSoundType(SoundType.WOOD);
    }


    @Override
    public String getToolType(Wood type) {
        return "axe";
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        switch (getEnumFromState(state)) {
            case BRANCH_SMALL: return BRANCH_SMALL_AABB;
            case BRANCH_MEDIUM: return BRANCH_MEDIUM_AABB;
            case BRANCH_LARGE: return BRANCH_LARGE_AABB;
            case STUMP_SMALL: return STUMP_SMALL_AABB;
            case STUMP_MEDIUM: return STUMP_MEDIUM_AABB;
            case STUMP_LARGE: return STUMP_LARGE_AABB;
            default: return STUMP_LARGE_AABB;
        }
    }

    @Override
    public ToolType getToolType() {
        return AXE;
    }

    @Override
    public ItemStack getDrop(EntityPlayer player, World world, BlockPos pos, IBlockState state, float luck) {
        Wood type = getEnumFromState(state);
        switch (type) {
            case BRANCH_SMALL: return new ItemStack(Blocks.LOG, 1);
            case BRANCH_MEDIUM: return new ItemStack(Blocks.LOG, 2);
            case BRANCH_LARGE: return new ItemStack(Blocks.LOG, 6);
            case STUMP_SMALL: return new ItemStack(Blocks.LOG, 3);
            case STUMP_MEDIUM: return new ItemStack(Blocks.LOG, 4);
            case STUMP_LARGE: return new ItemStack(Blocks.LOG, 12);
            default: return null;
        }
    }

    @Override
    public ToolTier getRequiredTier(IBlockState state) {
        switch (getEnumFromState(state)) {
            case BRANCH_SMALL: return BASIC;
            case BRANCH_MEDIUM: return COPPER;
            case BRANCH_LARGE: return MYSTRIL;
            case STUMP_SMALL: return SILVER;
            case STUMP_MEDIUM: return GOLD;
            case STUMP_LARGE: return CURSED;
            default: return null;
        }
    }

    @Override
    public int getSortValue(ItemStack stack) {
        return CreativeSort.TOOLS - 50 + stack.getItemDamage();
    }
}