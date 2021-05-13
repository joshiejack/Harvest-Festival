package uk.joshiejack.furniture.block;

import uk.joshiejack.furniture.Furniture;
import uk.joshiejack.furniture.block.properties.PropertyTVProgram;
import uk.joshiejack.furniture.client.renderer.statemap.TVStateMapper;
import uk.joshiejack.furniture.tile.TileTelevision;
import uk.joshiejack.penguinlib.block.base.BlockMultiTileRotatable;
import uk.joshiejack.penguinlib.tile.TileRotatable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
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
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Locale;

public class BlockTelevision extends BlockMultiTileRotatable<BlockTelevision.Model> {
    private static final AxisAlignedBB NORTH_AABB = new AxisAlignedBB(0D, 0D, 0D, 1D, 1D, 1D);
    private static final AxisAlignedBB SOUTH_AABB = new AxisAlignedBB(0D, 0D, 0D, 1D, 1D, 1D);
    private static final AxisAlignedBB EAST_AABB = new AxisAlignedBB(0.0625D, 0D, 0.125D, 0.625D, 0.9375D, 0.875D);
    private static final AxisAlignedBB WEST_AABB = new AxisAlignedBB(0D, 0D, 0D, 1D, 1D, 1D);

    public BlockTelevision() {
        super(new ResourceLocation(Furniture.MODID, "television"), Material.WOOD, Model.class);
        setHardness(1.5F);
        setSoundType(SoundType.WOOD);
        setCreativeTab(Furniture.INSTANCE);
    }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState() {
        if (property == null)
            return new ExtendedBlockState(this, new IProperty[]{temporary, FACING}, new IUnlistedProperty[]{ PropertyTVProgram.INSTANCE });
        return new ExtendedBlockState(this, new IProperty[]{property, FACING}, new IUnlistedProperty[]{ PropertyTVProgram.INSTANCE });
    }

    @SuppressWarnings("deprecation")
    @Nonnull
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileRotatable) {
            switch (((TileRotatable)tile).getFacing()) {
                case NORTH:
                    return NORTH_AABB;
                case EAST:
                    return EAST_AABB;
                case SOUTH:
                    return new AxisAlignedBB(0D, 0D, 0D, 1D, 1D, 1D);
                case WEST:
                    return new AxisAlignedBB(0D, 0D, 0D, 1D, 1D, 1D);
            }
        } return super.getBoundingBox(state, world, pos);
    }

    @SuppressWarnings("deprecation")
    @Nonnull
    @Override
    public IBlockState getExtendedState(@Nonnull IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity tile = world instanceof ChunkCache ? ((ChunkCache) world).getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK) : world.getTileEntity(pos);
        if (tile instanceof TileTelevision && state instanceof IExtendedBlockState) {  // avoid crash in case of mismatch
            return ((IExtendedBlockState) state).withProperty(PropertyTVProgram.INSTANCE, ((TileTelevision)tile).getProgram());
        } else return super.getExtendedState(state, world, pos);
    }

    @Nonnull
    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        return new TileTelevision();
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }

    @SuppressWarnings("ConstantConditions")
    @SideOnly(Side.CLIENT)
    @Override
    public void registerModels(Item item) {
        super.registerModels(item);
        ModelLoader.setCustomStateMapper(this, TVStateMapper.INSTANCE);
    }

    public enum Model implements IStringSerializable {
        STANDARD;

        @Override
        public String getName() {
            return name().toLowerCase(Locale.ENGLISH);
        }
    }
}
