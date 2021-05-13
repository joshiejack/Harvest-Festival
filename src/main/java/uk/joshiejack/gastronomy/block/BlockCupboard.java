package uk.joshiejack.gastronomy.block;

import uk.joshiejack.gastronomy.Gastronomy;
import uk.joshiejack.gastronomy.tile.TileCupboard;
import uk.joshiejack.penguinlib.block.base.BlockMultiTileRotatable;
import uk.joshiejack.penguinlib.util.interfaces.Rotatable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Locale;
import java.util.Objects;

import static net.minecraft.util.EnumFacing.*;

public class BlockCupboard extends BlockMultiTileRotatable<BlockCupboard.Cupboard> {
    public BlockCupboard() {
        super(new ResourceLocation(Gastronomy.MODID, "cupboard"), Material.WOOD, Cupboard.class);
        setHardness(2.5F);
        setSoundType(SoundType.WOOD);
        setCreativeTab(Gastronomy.TAB);
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing face) {
        return ((TileCupboard)Objects.requireNonNull(world.getTileEntity(pos))).getFacing() == face ? BlockFaceShape.CENTER : BlockFaceShape.SOLID;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean shouldSideBeRendered(IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, EnumFacing side) {
        return true; //TODO
    }

    @Nonnull
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileCupboard) {
            switch (((TileCupboard) tile).getFacing()) {
                case NORTH:
                    return new AxisAlignedBB(0D, 0D, 0.375D, 1D, 1D, 1D);
                case SOUTH:
                    return new AxisAlignedBB(0D, 0D, 0D, 1D, 1D, 0.625D);
                case WEST:
                    return new AxisAlignedBB(0.375D, 0D, 0D, 1D, 1D, 1D);
                default:
                    return new AxisAlignedBB(0D, 0D, 0D, 0.625D, 1D, 1D);
            }
        } else return super.getBoundingBox(state, world, pos);
    }

    @Nonnull
    @Override
    public IBlockState getActualState(@Nonnull IBlockState state, IBlockAccess world, BlockPos pos) {
        IBlockState ret = super.getActualState(state, world, pos);
        EnumFacing northFacing = getFacing(NORTH, world, pos);
        EnumFacing eastFacing = getFacing(EAST, world, pos);
        EnumFacing southFacing = getFacing(SOUTH, world, pos);
        EnumFacing westFacing = getFacing(WEST, world, pos);

        //Inner Corner
        if (northFacing == WEST && westFacing == NORTH)
            return state.withProperty(property, Cupboard.FULL_IC).withProperty(FACING, WEST);
        if (southFacing == WEST && westFacing == SOUTH)
            return state.withProperty(property, Cupboard.FULL_IC).withProperty(FACING, SOUTH);
        if (southFacing == EAST && eastFacing == SOUTH)
            return state.withProperty(property, Cupboard.FULL_IC).withProperty(FACING, EAST);
        if (northFacing == EAST && eastFacing == NORTH)
            return state.withProperty(property, Cupboard.FULL_IC).withProperty(FACING, NORTH);

        //Outer Corner
        if (northFacing == EAST && westFacing == SOUTH)
            return state.withProperty(property, Cupboard.FULL_OC).withProperty(FACING, EAST);
        if (southFacing == EAST && westFacing == NORTH)
            return state.withProperty(property, Cupboard.FULL_OC).withProperty(FACING, NORTH);
        if (southFacing == WEST && eastFacing == NORTH)
            return state.withProperty(property, Cupboard.FULL_OC).withProperty(FACING, WEST);
        if (northFacing == WEST && eastFacing == SOUTH)
            return state.withProperty(property, Cupboard.FULL_OC).withProperty(FACING, SOUTH);

        return ret;
    }

    private EnumFacing getFacing(EnumFacing facing, IBlockAccess world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos.offset(facing));
        if (tile instanceof TileCupboard) {
            return ((Rotatable) tile).getFacing();
        }

        return EnumFacing.DOWN;
    }

    @Nonnull
    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        return new TileCupboard();
    }

    public enum Cupboard implements IStringSerializable {
        FULL, FULL_IC, FULL_OC;

        @Override
        public String getName() {
            return name().toLowerCase(Locale.ENGLISH);
        }
    }
}
