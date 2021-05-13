package uk.joshiejack.penguinlib.block.custom;

import com.google.common.collect.Sets;
import uk.joshiejack.penguinlib.client.renderer.block.statemap.StateMapperFloor;
import uk.joshiejack.penguinlib.data.custom.block.CustomBlockFloorWithOverlays;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Locale;
import java.util.Set;

import static uk.joshiejack.penguinlib.block.custom.BlockCustomFloorWithOverlays.TextureStyle.*;

public class BlockCustomFloorWithOverlays extends BlockCustomSingular {
    private static final PropertyEnum<TextureStyle> NORTH_EAST = PropertyEnum.create("ne", TextureStyle.class);
    private static final PropertyEnum<TextureStyle> NORTH_WEST = PropertyEnum.create("nw", TextureStyle.class);
    private static final PropertyEnum<TextureStyle> SOUTH_EAST = PropertyEnum.create("se", TextureStyle.class);
    private static final PropertyEnum<TextureStyle> SOUTH_WEST = PropertyEnum.create("sw", TextureStyle.class);
    public static Set<BlockCustomFloorWithOverlays> BLOCKS = Sets.newHashSet();
    public final CustomBlockFloorWithOverlays.FloorOverlay[] overlays;

    @SuppressWarnings("ConstantConditions")
    public BlockCustomFloorWithOverlays(ResourceLocation registryName, CustomBlockFloorWithOverlays data) {
        super(registryName, data);
        setDefaultState(getDefaultState()
                .withProperty(NORTH_EAST, OUTER)
                .withProperty(NORTH_WEST, OUTER)
                .withProperty(SOUTH_EAST, OUTER)
                .withProperty(SOUTH_WEST, OUTER));
        overlays = data.overlays;
        BLOCKS.add(this);
    }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, NORTH_EAST, NORTH_WEST, SOUTH_EAST, SOUTH_WEST);
    }

    private TextureStyle getStateFromBoolean(boolean one, boolean two, boolean three) {
        if (one && !two && !three) return VERTICAL;
        if (!one && two && !three) return  HORIZONTAL;
        if (one && two && !three) return INNER;
        if (!one && two) return HORIZONTAL;
        if (one && !two) return VERTICAL;
        if (one) return BLANK;
        return OUTER;
    }

    private boolean isSameBlock(IBlockAccess world, BlockPos pos) {
        return world.getBlockState(pos).getBlock() == this;
    }

    @Override
    public boolean canSustainPlant(@Nonnull IBlockState state, @Nonnull IBlockAccess world, BlockPos pos, @Nonnull EnumFacing direction, IPlantable plantable) {
        EnumPlantType up = plantable.getPlantType(world, pos.up());
        return up != EnumPlantType.Nether && up != EnumPlantType.Water && up != EnumPlantType.Beach;
    }

    @SideOnly(Side.CLIENT)
    @Nonnull
    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isFullCube(IBlockState blockState) {
        return true;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isOpaqueCube(IBlockState blockState) {
        return false;
    }

    @Nonnull
    @Override
    public IBlockState getExtendedState(@Nonnull IBlockState state, IBlockAccess world, BlockPos pos) {
        return getActualState(state, world, pos);
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.SOLID;
    }

    @SuppressWarnings("deprecation, unchecked")
    @Override
    @Nonnull
    public IBlockState getActualState(@Nonnull IBlockState state, IBlockAccess world, BlockPos pos) {
        boolean north = isSameBlock(world, pos.north());
        boolean west = isSameBlock(world, pos.west());
        boolean south = isSameBlock(world, pos.south());
        boolean east = isSameBlock(world, pos.east());
        boolean northEast = north && east && isSameBlock(world, pos.north().east());
        boolean northWest = north && west && isSameBlock(world, pos.north().west());
        boolean southEast = south && east && isSameBlock(world, pos.south().east());
        boolean southWest = south && west && isSameBlock(world, pos.south().west());
        TextureStyle ne = getStateFromBoolean(north, east, northEast);
        TextureStyle nw = getStateFromBoolean(north, west, northWest);
        TextureStyle se = getStateFromBoolean(south, east, southEast);
        TextureStyle sw = getStateFromBoolean(south, west, southWest);
        return state.withProperty(NORTH_EAST, ne).withProperty(NORTH_WEST, nw).withProperty(SOUTH_EAST, se).withProperty(SOUTH_WEST, sw);
    }

    @SuppressWarnings("ConstantConditions")
    @SideOnly(Side.CLIENT)
    @Override
    public void registerModels(Item item) {
        ModelLoader.setCustomStateMapper(this, new StateMapperFloor());
        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(getRegistryName(), "ne=outer,nw=outer,se=outer,sw=outer"));
    }

    public enum TextureStyle implements IStringSerializable {
        BLANK, INNER, VERTICAL, HORIZONTAL, OUTER;

        @Override
        public String getName() {
            return name().toLowerCase(Locale.ENGLISH);
        }
    }
}
