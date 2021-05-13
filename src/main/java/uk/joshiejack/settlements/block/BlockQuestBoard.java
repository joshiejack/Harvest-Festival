package uk.joshiejack.settlements.block;

import uk.joshiejack.settlements.Settlements;
import uk.joshiejack.settlements.quest.settings.QuestBoardClient;
import uk.joshiejack.settlements.tile.TileQuestBoard;
import uk.joshiejack.penguinlib.block.base.BlockMultiRotatable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Locale;


public class BlockQuestBoard extends BlockMultiRotatable<BlockQuestBoard.Material> {
    private static final AxisAlignedBB WEST = new AxisAlignedBB(0.8D, 0D, 0D, 1.0D, 1D, 1D);
    private static final AxisAlignedBB SOUTH = new AxisAlignedBB(0D, 0D, 0D, 1D, 1D, 0.2D);
    private static final AxisAlignedBB EAST = new AxisAlignedBB(0D, 0D, 0D, 0.2D, 1D, 1D);
    private static final AxisAlignedBB NORTH = new AxisAlignedBB(0D, 0D, 0.8D, 1D, 1D, 1D);

    public BlockQuestBoard() {
        super(new ResourceLocation(Settlements.MODID, "quest_board"), net.minecraft.block.material.Material.WOOD, Material.class);
        setCreativeTab(Settlements.TAB);
    }

    public EnumFacing getFacing(IBlockState state) {
        return state.getValue(FACING);
    }

    @SuppressWarnings("deprecation")
    @Override
    @Nonnull
    public AxisAlignedBB getBoundingBox(IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos) {
        EnumFacing facing = state.getValue(FACING);
        switch (facing) {
            case NORTH: return BlockQuestBoard.NORTH;
            case EAST: return BlockQuestBoard.EAST;
            case WEST: return BlockQuestBoard.WEST;
            case SOUTH: return BlockQuestBoard.SOUTH;
            default:
                return FULL_BLOCK_AABB;
        }
    }

    @Override
    public boolean hasTileEntity(@Nonnull IBlockState state) {
        return true;
    }

    @Nonnull
    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        return new TileQuestBoard();
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    public IBlockState getActualState(@Nonnull IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos) {
        if (QuestBoardClient.hasDailyQuest()) {
            return state.withProperty(property, Material.DEFAULT);
        }

        return state;
    }

    @Override
    public boolean onBlockActivated(@Nonnull World world, BlockPos pos, @Nonnull IBlockState state, EntityPlayer player,
                                    @Nonnull EnumHand hand, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ) {
        player.openGui(Settlements.instance, Settlements.QUEST_BOARD, player.world, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }

    public enum Material implements IStringSerializable {
        DEFAULT;

        @Override
        public String getName() {
            return name().toLowerCase(Locale.ENGLISH);
        }
    }
}
