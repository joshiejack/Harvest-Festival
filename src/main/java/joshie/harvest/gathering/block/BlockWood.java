package joshie.harvest.gathering.block;

import com.google.common.collect.Lists;
import joshie.harvest.api.core.ITiered.ToolTier;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.base.block.BlockHFSmashable;
import joshie.harvest.core.base.item.ItemToolSmashing;
import joshie.harvest.core.lib.CreativeSort;
import joshie.harvest.core.util.interfaces.ISellable;
import joshie.harvest.gathering.block.BlockWood.Wood;
import joshie.harvest.tools.HFTools;
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

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static joshie.harvest.api.core.ITiered.ToolTier.*;
import static joshie.harvest.api.gathering.ISmashable.ToolType.AXE;

public class BlockWood extends BlockHFSmashable<BlockWood, Wood> {
    private static final AxisAlignedBB BRANCH_SMALL_AABB = new AxisAlignedBB(0.15D, 0.0D, 0.15D, 0.85D, 0.15D, 0.85D);
    private static final AxisAlignedBB BRANCH_MEDIUM_AABB = new AxisAlignedBB(0.15D, 0.0D, 0.15D, 0.85D, 0.25D, 0.85D);
    private static final AxisAlignedBB BRANCH_LARGE_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.9D, 1.0D);
    private static final AxisAlignedBB STUMP_SMALL_AABB = new AxisAlignedBB(0.2D, 0.0D, 0.2D, 0.8D, 0.25D, 0.8D);
    private static final AxisAlignedBB STUMP_MEDIUM_AABB = new AxisAlignedBB(0.1D, 0.0D, 0.1D, 0.9D, 0.35D, 0.9D);
    private static final AxisAlignedBB STUMP_LARGE_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);

    public enum Wood implements IStringSerializable, ISellable {
        BRANCH_SMALL(1L), BRANCH_MEDIUM(2L), BRANCH_LARGE(4L), STUMP_SMALL(3L), STUMP_MEDIUM(5L), STUMP_LARGE(10L);

        private final long sell;

        Wood(long sell) {
            this.sell = sell;
        }

        @Override
        public long getSellValue() {
            return sell;
        }

        @Override
        public String getName() {
            return toString().toLowerCase(Locale.ENGLISH);
        }
    }

    public BlockWood() {
        super(Material.WOOD, Wood.class, HFTab.GATHERING);
        setHardness(2F);
        setSoundType(SoundType.WOOD);
    }


    @Override
    public String getToolType(Wood type) {
        return "axe";
    }

    @SuppressWarnings("deprecation")
    @Override
    @Nonnull
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
    public ItemToolSmashing getTool() {
        return HFTools.AXE;
    }

    @Override
    public List<ItemStack> getDrops(EntityPlayer player, World world, BlockPos pos, IBlockState state, float luck) {
        Wood type = getEnumFromState(state);
        switch (type) {
            case BRANCH_SMALL: return Lists.newArrayList(new ItemStack(Blocks.LOG, 1));
            case BRANCH_MEDIUM: return Lists.newArrayList(new ItemStack(Blocks.LOG, 2));
            case BRANCH_LARGE: return Lists.newArrayList(new ItemStack(Blocks.LOG, 6));
            case STUMP_SMALL: return Lists.newArrayList(new ItemStack(Blocks.LOG, 3));
            case STUMP_MEDIUM: return Lists.newArrayList(new ItemStack(Blocks.LOG, 4));
            case STUMP_LARGE: return Lists.newArrayList(new ItemStack(Blocks.LOG, 12));
            default: return new ArrayList<>();
        }
    }

    @Override
    public ToolTier getRequiredTier(IBlockState state) {
        switch (getEnumFromState(state)) {
            case BRANCH_SMALL: return BASIC;
            case BRANCH_MEDIUM: return COPPER;
            case BRANCH_LARGE: return GOLD;
            case STUMP_SMALL: return SILVER;
            case STUMP_MEDIUM: return GOLD;
            case STUMP_LARGE: return MYSTRIL;
            default: return null;
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public float getBlockHardness(IBlockState state, World world, BlockPos pos) {
        return getToolLevel(getEnumFromState(state));
    }

    @Override
    public int getSortValue(ItemStack stack) {
        return CreativeSort.TOOLS - 50 + stack.getItemDamage();
    }
}