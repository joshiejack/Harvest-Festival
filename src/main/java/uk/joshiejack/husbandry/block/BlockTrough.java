package uk.joshiejack.husbandry.block;

import uk.joshiejack.husbandry.Husbandry;
import uk.joshiejack.husbandry.tile.TileTrough;
import uk.joshiejack.penguinlib.block.base.BlockMultiTileRotatable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;

import static uk.joshiejack.husbandry.Husbandry.MODID;
import static net.minecraft.util.EnumFacing.*;

public class BlockTrough extends BlockMultiTileRotatable<BlockTrough.Section> {
    public static final AxisAlignedBB FENCE_COLLISION =  new AxisAlignedBB(0D, 0D, 0D, 1D, 1.5D, 1D);
    private static final AxisAlignedBB TROUGH_AABB =  new AxisAlignedBB(0D, 0D, 0D, 1D, 0.75D, 1D);

    public BlockTrough() {
        super(new ResourceLocation(MODID, "trough"), Material.WOOD, Section.class);
        setHardness(1.5F);
        setSoundType(SoundType.WOOD);
        setCreativeTab(Husbandry.TAB);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void addCollisionBoxToList(@Nonnull IBlockState state, @Nonnull World world, @Nonnull BlockPos pos,
                                      @Nonnull AxisAlignedBB entityBox, @Nonnull List<AxisAlignedBB> collidingBoxes,
                                      @Nullable Entity entityIn, boolean isActualState) {
        if (entityIn instanceof EntityPlayer) addCollisionBoxToList(pos, entityBox, collidingBoxes, TROUGH_AABB);
        else addCollisionBoxToList(pos, entityBox, collidingBoxes, FENCE_COLLISION);
    }

    @SuppressWarnings("deprecation")
    @Nonnull
    @Override
    public AxisAlignedBB getBoundingBox(@Nonnull IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos) {
        return TROUGH_AABB;
    }

    @Override
    @Nonnull
    public IBlockState getActualState(@Nonnull IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity tile = world instanceof ChunkCache ? ((ChunkCache)world).getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK) : world.getTileEntity(pos);
        if (tile instanceof TileTrough) {
            boolean north = isTrough(NORTH, world, pos);
            boolean south = isTrough(SOUTH, world, pos);
            if (north && !south) return state.withProperty(property, Section.END).withProperty(FACING, EAST);
            if (south && !north) return state.withProperty(property, Section.END).withProperty(FACING, WEST);
            if (south) return state.withProperty(property, Section.MIDDLE).withProperty(FACING, EAST);

            boolean east = isTrough(EAST, world, pos);
            boolean west = isTrough(WEST, world, pos);
            if (west && east) return state.withProperty(property, Section.MIDDLE).withProperty(FACING, SOUTH);
            if (east) return state.withProperty(property, Section.END).withProperty(FACING, SOUTH);
            if (west) return state.withProperty(property, Section.END).withProperty(FACING, NORTH);

            return state.withProperty(property, Section.SINGLE);
        }

        return state;
    }

    @SuppressWarnings("ConstantConditions")
    private boolean isTrough(EnumFacing facing, IBlockAccess world, BlockPos pos) {
        return world.getBlockState(pos.offset(facing)).getBlock() == this && (((TileTrough) world.getTileEntity(pos)).getMasterBlock() == ((TileTrough) world.getTileEntity(pos.offset(facing))).getMasterBlock());
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        super.onBlockPlacedBy(world, pos, state, placer, stack);
        ((TileTrough)world.getTileEntity(pos)).onBlockPlaced();
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void breakBlock(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        ((TileTrough)world.getTileEntity(pos)).onBlockRemoved();
        super.breakBlock(world, pos, state);
    }

    @Nonnull
    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        return new TileTrough();
    }

    @Nonnull
    @Override
    protected ItemStack getCreativeStack(Section section) {
        return section == Section.SINGLE ? super.getCreativeStack(section) : ItemStack.EMPTY;
    }

    public enum Section implements IStringSerializable {
        SINGLE, END, MIDDLE;

        @Override
        public @Nonnull String getName() {
            return toString().toLowerCase(Locale.ENGLISH);
        }
    }
}
