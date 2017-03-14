package joshie.harvest.gathering.block;

import com.google.common.collect.Lists;
import joshie.harvest.api.core.ITiered.ToolTier;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.base.block.BlockHFSmashable;
import joshie.harvest.core.base.item.ItemToolSmashing;
import joshie.harvest.core.lib.CreativeSort;
import joshie.harvest.core.util.interfaces.ISellable;
import joshie.harvest.gathering.block.BlockRock.Rock;
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

import static joshie.harvest.api.gathering.ISmashable.ToolType.HAMMER;

public class BlockRock extends BlockHFSmashable<BlockRock, Rock> {
    private static final AxisAlignedBB STONE_SMALL_AABB = new AxisAlignedBB(0.15D, 0.0D, 0.15D, 0.85D, 0.15D, 0.85D);
    private static final AxisAlignedBB STONE_MEDIUM_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.2D, 1.0D);
    private static final AxisAlignedBB STONE_LARGE_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.3D, 1.0D);
    private static final AxisAlignedBB BOULDER_SMALL_AABB = new AxisAlignedBB(0.225D, 0.0D, 0.225D, 0.775D, 0.25D, 0.775D);
    private static final AxisAlignedBB BOULDER_MEDIUM_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.35D, 1.0D);
    private static final AxisAlignedBB BOULDER_LARGE_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.6D, 1.0D);

    public enum Rock implements IStringSerializable, ISellable {
        STONE_SMALL(1L), STONE_MEDIUM(2L), STONE_LARGE(4L), BOULDER_SMALL(3L), BOULDER_MEDIUM(5L), BOULDER_LARGE(10L);

        private final long sell;

        Rock(long sell) {
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

    public BlockRock() {
        super(Material.ROCK, Rock.class, HFTab.GATHERING);
        setHardness(1.5F);
        setSoundType(SoundType.STONE);
    }

    @SuppressWarnings("deprecation")
    @Override
    @Nonnull
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        switch (getEnumFromState(state)) {
            case STONE_SMALL: return STONE_SMALL_AABB;
            case STONE_MEDIUM: return STONE_MEDIUM_AABB;
            case STONE_LARGE: return STONE_LARGE_AABB;
            case BOULDER_SMALL: return BOULDER_SMALL_AABB;
            case BOULDER_MEDIUM: return BOULDER_MEDIUM_AABB;
            case BOULDER_LARGE: return BOULDER_LARGE_AABB;
            default: return BOULDER_LARGE_AABB;
        }
    }

    @Override
    public ToolType getToolType() {
        return HAMMER;
    }

    @Override
    public ItemToolSmashing getTool() {
        return HFTools.HAMMER;
    }

    @Override
    public List<ItemStack> getDrops(EntityPlayer player, World world, BlockPos pos, IBlockState state, float luck) {
        switch (getEnumFromState(state)) {
            case STONE_SMALL: return Lists.newArrayList(new ItemStack(Blocks.STONE, 1));
            case STONE_MEDIUM: return Lists.newArrayList(new ItemStack(Blocks.STONE, 2));
            case STONE_LARGE: return Lists.newArrayList(new ItemStack(Blocks.STONE, 4));
            case BOULDER_SMALL: return Lists.newArrayList(new ItemStack(Blocks.STONE, 3));
            case BOULDER_MEDIUM: return Lists.newArrayList(new ItemStack(Blocks.STONE, 6));
            case BOULDER_LARGE: return Lists.newArrayList(new ItemStack(Blocks.STONE, 12));
            default: return new ArrayList<>();
        }
    }

    @Override
    public ToolTier getRequiredTier(IBlockState state) {
        return ToolTier.BASIC;
    }

    @SuppressWarnings("deprecation")
    @Deprecated
    public float getBlockHardness(IBlockState state, World world, BlockPos pos) {
        return getToolLevel(getEnumFromState(state)) * 2;
    }

    @Override
    public int getSortValue(ItemStack stack) {
        return CreativeSort.TOOLS - 30 + stack.getItemDamage();
    }
}