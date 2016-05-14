package joshie.harvest.blocks;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.blocks.BlockGathering.GatheringType;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.lib.CreativeSort;
import joshie.harvest.core.util.base.BlockHFBaseEnumRotatableMeta;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import static joshie.harvest.blocks.BlockGathering.GatheringType.ROCK;

public class BlockGathering extends BlockHFBaseEnumRotatableMeta<GatheringType> {
    public static final PropertyBool WINTER = PropertyBool.create("winter");
    public static final PropertyBool DECORATIVE = PropertyBool.create("decorative");
    public enum GatheringType implements IStringSerializable {
        BRANCH, STUMP, ROCK;

        @Override
        public String getName() {
            return toString().toLowerCase();
        }
    }

    public BlockGathering() {
        super(Material.WOOD, GatheringType.class, HFTab.GATHERING);
        setHardness(1.5F);
        setSoundType(SoundType.WOOD);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        if(property == null) return new BlockStateContainer(this, temporary, FACING, WINTER, DECORATIVE);
        return new BlockStateContainer(this, property, FACING, WINTER, DECORATIVE);
    }

    @Override
    public String getToolType(GatheringType type) {
        return type == ROCK ? "pickaxe" : "axe";
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        GatheringType type = getEnumFromState(state);
        switch (type) {
            case BRANCH:
                return new AxisAlignedBB(0D, 0D, 0D, 1D, 0.6D, 1D);
            default:
                return FULL_BLOCK_AABB;
        }
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        Season season = HFApi.calendar.getToday().getSeason();
        if (season == Season.WINTER) {
            return state.withProperty(WINTER, true);
        } else return state.withProperty(WINTER, false);
    }

    @Override
    public int getSortValue(ItemStack stack) {
        return CreativeSort.TROUGH;
    }
}