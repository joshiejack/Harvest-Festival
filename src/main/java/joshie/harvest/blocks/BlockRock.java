package joshie.harvest.blocks;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.core.ITiered.ToolTier;
import joshie.harvest.api.gathering.ISmashable;
import joshie.harvest.blocks.BlockRock.Rock;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.helpers.WorldHelper;
import joshie.harvest.core.lib.CreativeSort;
import joshie.harvest.core.util.base.BlockHFEnum;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import static joshie.harvest.api.core.ITiered.ToolTier.*;
import static joshie.harvest.api.gathering.ISmashable.ToolType.HAMMER;

public class BlockRock extends BlockHFEnum<BlockRock, Rock> implements ISmashable {
    public static final PropertyBool WINTER = PropertyBool.create("winter");

    public enum Rock implements IStringSerializable {
        STONE_SMALL, STONE_MEDIUM, STONE_LARGE, BOULDER_SMALL, BOULDER_MEDIUM, BOULDER_LARGE;

        @Override
        public String getName() {
            return toString().toLowerCase();
        }
    }

    public BlockRock() {
        super(Material.ROCK, Rock.class, HFTab.GATHERING);
        setHardness(1.5F);
        setSoundType(SoundType.STONE);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        if(property == null) return new BlockStateContainer(this, temporary, WINTER);
        return new BlockStateContainer(this, property, WINTER);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        Season season = HFApi.calendar.getDate(WorldHelper.getWorld(world)).getSeason();
        if (season == Season.WINTER) {
            return state.withProperty(WINTER, true);
        } else return state.withProperty(WINTER, false);
    }

    @Override
    public ToolType getToolType() {
        return HAMMER;
    }

    @Override
    public ItemStack getDrop(EntityPlayer player, World world, BlockPos pos, IBlockState state, float luck) {
        Rock type = getEnumFromState(state);
        switch (type) {
            case STONE_SMALL: return new ItemStack(Blocks.STONE, 1);
            case STONE_MEDIUM: return new ItemStack(Blocks.STONE, 2);
            case STONE_LARGE: return new ItemStack(Blocks.STONE, 4);
            case BOULDER_SMALL: return new ItemStack(Blocks.STONE, 3);
            case BOULDER_MEDIUM: return new ItemStack(Blocks.STONE, 6);
            case BOULDER_LARGE: return new ItemStack(Blocks.STONE, 12);
            default: return null;
        }
    }

    @Override
    public ToolTier getRequiredTier(IBlockState state) {
        switch (getEnumFromState(state)) {
            case STONE_SMALL: return BASIC;
            case STONE_MEDIUM: return COPPER;
            case STONE_LARGE: return GOLD;
            case BOULDER_SMALL: return SILVER;
            case BOULDER_MEDIUM: return MYSTRIL;
            case BOULDER_LARGE: return CURSED;
            default: return null;
        }
    }

    @Override
    public int getSortValue(ItemStack stack) {
        return CreativeSort.TOOLS - 2;
    }
}