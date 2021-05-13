package uk.joshiejack.husbandry.block;

import uk.joshiejack.husbandry.Husbandry;
import uk.joshiejack.husbandry.tile.TileBowl;
import uk.joshiejack.husbandry.tile.TileFeeder;
import uk.joshiejack.husbandry.tile.TileFoodSupply;
import uk.joshiejack.husbandry.tile.TileNest;
import uk.joshiejack.penguinlib.block.base.BlockMultiTile;
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

public class BlockTray extends BlockMultiTile<BlockTray.Tray> {
    private static final AxisAlignedBB NEST_COLLISION = new AxisAlignedBB(0.15D, 0D, 0.15D, 0.85D, 0.2D, 0.85D);
    private static final AxisAlignedBB NEST_AABB = new AxisAlignedBB(0.15D, 0D, 0.15D, 0.85D, 0.35D, 0.85D);
    private static final AxisAlignedBB FEEDER_AABB = new AxisAlignedBB(0.0D, 0D, 0.0D, 1.0D, 0.075D, 1.0D);

    public BlockTray() {
        super(new ResourceLocation(MODID, "tray"), Material.WOOD, Tray.class);
        setHardness(0.5F);
        setSoundType(SoundType.WOOD);
        setCreativeTab(Husbandry.TAB);
    }

    @SuppressWarnings("deprecation")
    @Override
    public AxisAlignedBB getCollisionBoundingBox(@Nonnull IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos) {
        Tray tray = getEnumFromState(state);
        return tray == Tray.FEEDER_EMPTY || tray == Tray.FEEDER_SEMI || tray == Tray.FEEDER_FULL ? super.getCollisionBoundingBox(state, world, pos) : NEST_COLLISION;
    }

    @SuppressWarnings("deprecation")
    @Nonnull
    @Override
    public AxisAlignedBB getBoundingBox(@Nonnull IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos) {
        Tray tray = getEnumFromState(state);
        return tray == Tray.FEEDER_EMPTY || tray == Tray.FEEDER_SEMI || tray == Tray.FEEDER_FULL ? FEEDER_AABB : NEST_AABB;
    }

    @SuppressWarnings("deprecation")
    @Nonnull
    @Override
    public IBlockState getActualState(@Nonnull IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos) {
        TileEntity tile = world instanceof ChunkCache ? ((ChunkCache)world).getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK) : world.getTileEntity(pos);
        if (tile instanceof TileFoodSupply) {
            return getStateFromEnum(((TileFoodSupply)tile).getTrayState());
        } else if (tile instanceof TileNest) {
            return getStateFromEnum(((TileNest)tile).getTrayState());
        } else return state;
    }

    @Nonnull
    @Override
    protected ItemStack getCreativeStack(BlockTray.Tray tray) {
        return tray == Tray.NEST_EMPTY || tray == Tray.FEEDER_EMPTY || tray == Tray.BOWL_EMPTY ? super.getCreativeStack(tray) : ItemStack.EMPTY;
    }

    @Nonnull
    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        switch (getEnumFromState(state)) {
            case NEST_EMPTY:
            case NEST_LARGE:
            case NEST_MEDIUM:
            case NEST_SMALL:
                return new TileNest();
            case FEEDER_EMPTY:
            case FEEDER_SEMI:
            case FEEDER_FULL:
                return new TileFeeder();
            case BOWL_EMPTY:
            case BOWL_CAT:
            case BOWL_DOG:
            case BOWL_RABBIT:
            default:
                return new TileBowl();
        }
    }

    public enum Tray implements IStringSerializable {
        NEST_EMPTY, NEST_SMALL, NEST_MEDIUM, NEST_LARGE,
        FEEDER_EMPTY, FEEDER_SEMI, FEEDER_FULL,
        BOWL_EMPTY, BOWL_CAT, BOWL_DOG, BOWL_RABBIT;

        @Override
        public @Nonnull String getName() {
            return name().toLowerCase(Locale.ENGLISH);
        }
    }
}
