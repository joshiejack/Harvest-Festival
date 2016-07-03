package joshie.harvest.blocks;

import joshie.harvest.api.core.ITiered.ToolTier;
import joshie.harvest.api.gathering.ISmashable;
import joshie.harvest.blocks.BlockWood.Wood;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.lib.CreativeSort;
import joshie.harvest.core.util.base.BlockHFEnum;
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
import static joshie.harvest.blocks.BlockWood.Wood.*;

public class BlockWood extends BlockHFEnum<BlockWood, Wood> implements ISmashable {
    public enum Wood implements IStringSerializable {
        BRANCH_SMALL, BRANCH_MEDIUM, BRANCH_LARGE, STUMP_SMALL, STUMP_MEDIUM, STUMP_LARGE;

        @Override
        public String getName() {
            return toString().toLowerCase();
        }
    }

    public BlockWood() {
        super(Material.WOOD, Wood.class, HFTab.GATHERING);
        setHardness(1.5F);
        setSoundType(SoundType.WOOD);
    }


    @Override
    public String getToolType(Wood type) {
        return "axe";
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        Wood type = getEnumFromState(state);
        switch (type) {
            case BRANCH_SMALL:
                return new AxisAlignedBB(0D, 0D, 0D, 1D, 0.6D, 1D);
            default:
                return FULL_BLOCK_AABB;
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
            case BRANCH_LARGE: return new ItemStack(Blocks.LOG, 4);
            case STUMP_SMALL: return new ItemStack(Blocks.LOG, 3);
            case STUMP_MEDIUM: return new ItemStack(Blocks.LOG, 6);
            case STUMP_LARGE: return new ItemStack(Blocks.LOG, 12);
            default: return null;
        }
    }

    @Override
    public ToolTier getRequiredTier(IBlockState state) {
        switch (getEnumFromState(state)) {
            case BRANCH_SMALL: return BASIC;
            case BRANCH_MEDIUM: return COPPER;
            case BRANCH_LARGE: return GOLD;
            case STUMP_SMALL: return SILVER;
            case STUMP_MEDIUM: return MYSTRIL;
            case STUMP_LARGE: return CURSED;
            default: return null;
        }
    }

    @Override
    public int getSortValue(ItemStack stack) {
        Wood type = getEnumFromMeta(stack.getItemDamage());
        if (type == BRANCH_SMALL || type == BRANCH_MEDIUM || type == BRANCH_LARGE) {
            return CreativeSort.TOOLS - 4;
        } else if (type == STUMP_SMALL || type == STUMP_MEDIUM || type == STUMP_LARGE) {
            return CreativeSort.TOOLS - 3;
        } return CreativeSort.TOOLS - 2;
    }
}