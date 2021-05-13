package uk.joshiejack.husbandry.block;

import uk.joshiejack.husbandry.Husbandry;
import uk.joshiejack.husbandry.tile.TileIncubator;
import uk.joshiejack.penguinlib.block.base.BlockMultiTileRotatable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import javax.annotation.Nonnull;
import java.util.Locale;

import static uk.joshiejack.husbandry.Husbandry.MODID;

public class BlockIncubator extends BlockMultiTileRotatable<BlockIncubator.Fill> {
    private static final AxisAlignedBB NEST_NORTH_AABB = new AxisAlignedBB(0.05D, 0D, 0.3D, 0.95D, 0.7D, 0.95);
    private static final AxisAlignedBB NEST_SOUTH_AABB = new AxisAlignedBB(0.05D, 0D, 0.05D, 0.95D, 0.7D, 0.7);
    private static final AxisAlignedBB NEST_WEST_AABB = new AxisAlignedBB(0.3D, 0D, 0.05D, 0.95D, 0.7D, 0.95);
    private static final AxisAlignedBB NEST_EAST_AABB = new AxisAlignedBB(0.05D, 0D, 0.05D, 0.7D, 0.7D, 0.95);

    public BlockIncubator() {
        super(new ResourceLocation(MODID, "incubator"), Material.PISTON, Fill.class);
        setHardness(1.5F);
        setSoundType(SoundType.WOOD);
        setCreativeTab(Husbandry.TAB);
    }

    @SuppressWarnings("deprecation")
    @Nonnull
    @Override
    public AxisAlignedBB getBoundingBox(@Nonnull IBlockState state, IBlockAccess world, @Nonnull BlockPos pos) {
        TileIncubator tile = (TileIncubator) world.getTileEntity(pos);
        if (tile != null) {
            switch (tile.getFacing()) {
                case NORTH:
                    return NEST_NORTH_AABB;
                case SOUTH:
                    return NEST_SOUTH_AABB;
                case WEST:
                    return NEST_WEST_AABB;
                case EAST:
                    return NEST_EAST_AABB;
                default:
                    return FULL_BLOCK_AABB;
            }
        } else return FULL_BLOCK_AABB;
    }

    @Override
    public @Nonnull IBlockState getActualState(@Nonnull IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity tile = world instanceof ChunkCache ? ((ChunkCache)world).getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK) : world.getTileEntity(pos);
        if (tile instanceof TileIncubator) {
            Fill fill = ((TileIncubator)tile).getFill();
            return getStateFromEnum(fill).withProperty(FACING, state.getValue(FACING));
        }

        return super.getActualState(state, world, pos);
    }

    @Nonnull
    @Override
    protected ItemStack getCreativeStack(BlockIncubator.Fill fill) {
        return fill == Fill.EMPTY ? super.getCreativeStack(fill) : ItemStack.EMPTY;
    }

    @Nonnull
    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        return new TileIncubator();
    }

    public enum Fill implements IStringSerializable {
        EMPTY, SMALL, MEDIUM, LARGE;

        @Nonnull
        @Override
        public String getName() {
            return name().toLowerCase(Locale.ENGLISH);
        }
    }
}
