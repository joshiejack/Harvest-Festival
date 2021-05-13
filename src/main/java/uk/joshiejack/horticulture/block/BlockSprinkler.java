package uk.joshiejack.horticulture.block;

import uk.joshiejack.horticulture.Horticulture;
import uk.joshiejack.horticulture.tileentity.TileSprinklerIron;
import uk.joshiejack.horticulture.tileentity.TileSprinklerOld;
import uk.joshiejack.penguinlib.block.base.BlockMultiTile;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Locale;

import static uk.joshiejack.horticulture.Horticulture.MODID;

public class BlockSprinkler extends BlockMultiTile<BlockSprinkler.Sprinkler> {
    private static final AxisAlignedBB IRON_AABB = new AxisAlignedBB(0.2D, 0D, 0.2D, 0.8D, 0.7D, 0.8D);
    private static final AxisAlignedBB OLD_AABB = new AxisAlignedBB(0.2D, 0D, 0.2D, 0.8D, 0.5D, 0.8D);

    public BlockSprinkler() {
        super(new ResourceLocation(MODID, "sprinkler"), Material.PISTON, Sprinkler.class);
        setCreativeTab(Horticulture.TAB);
        setSoundType(SoundType.GROUND);
    }

    @SuppressWarnings("deprecation")
    @Override
    @Nonnull
    public AxisAlignedBB getBoundingBox(@Nonnull IBlockState state, @Nonnull IBlockAccess source, @Nonnull BlockPos pos) {
        return getEnumFromState(state) == Sprinkler.IRON ? IRON_AABB : OLD_AABB;
    }

    @SuppressWarnings("deprecation")
    @Override
    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(@Nonnull IBlockState blockState, @Nonnull IBlockAccess worldIn, @Nonnull BlockPos pos) {
        return NULL_AABB;
    }

    @Nonnull
    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        return getEnumFromState(state) == Sprinkler.OLD ? new TileSprinklerOld() : new TileSprinklerIron();
    }

    public enum Sprinkler implements IStringSerializable {
        OLD, IRON;

        @Nonnull
        @Override
        public String getName() {
            return name().toLowerCase(Locale.ENGLISH);
        }
    }
}
