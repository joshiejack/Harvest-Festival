package joshie.harvest.animals.blocks;

import joshie.harvest.animals.blocks.BlockSizedStorage.SizedStorage;
import joshie.harvest.api.core.ISizeable.Size;
import joshie.harvest.blocks.tiles.TileFillable;
import joshie.harvest.blocks.tiles.TileFillableSized;
import joshie.harvest.core.util.base.BlockHFEnumRotatableTile;
import joshie.harvest.core.util.generic.IFaceable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.EnumMap;

import static joshie.harvest.animals.blocks.BlockSizedStorage.Fill.*;

public class BlockSizedStorage extends BlockHFEnumRotatableTile<BlockSizedStorage, SizedStorage> {
    private static final AxisAlignedBB NEST_NORTH_AABB = new AxisAlignedBB(0.05D, 0D, 0.3D, 0.95D, 0.7D, 0.95);
    private static final AxisAlignedBB NEST_SOUTH_AABB = new AxisAlignedBB(0.05D, 0D, 0.05D, 0.95D, 0.7D, 0.7);
    private static final AxisAlignedBB NEST_WEST_AABB = new AxisAlignedBB(0.3D, 0D, 0.05D, 0.95D, 0.7D, 0.95);
    private static final AxisAlignedBB NEST_EAST_AABB = new AxisAlignedBB(0.05D, 0D, 0.05D, 0.7D, 0.7D, 0.95);

    public static final PropertyEnum<Fill> FILL = PropertyEnum.create("fill", Fill.class);

    public enum SizedStorage implements IStringSerializable {
        INCUBATOR;

        @Override
        public String getName() {
            return toString().toLowerCase();
        }
    }

    public enum Fill implements IStringSerializable {
        EMPTY, SMALL, MEDIUM, LARGE;

        public static final EnumMap<Size, Fill> conversion = new EnumMap(Size.class);

        @Override
        public String getName() {
            return toString().toLowerCase();
        }
    }

    static {
        conversion.put(Size.SMALL, SMALL);
        conversion.put(Size.MEDIUM, MEDIUM);
        conversion.put(Size.LARGE, LARGE);
    }

    public BlockSizedStorage() {
        super(Material.WOOD, SizedStorage.class);
        setHardness(1.5F);
        setSoundType(SoundType.WOOD);
        setDefaultState(getDefaultState().withProperty(FILL, Fill.EMPTY));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        if(property == null) return new BlockStateContainer(this, temporary, FACING, FILL);
        return new BlockStateContainer(this, property, FACING, FILL);
    }

    @Override
    public String getToolType(SizedStorage wood) {
        return "axe";
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        IFaceable tile = (IFaceable) world.getTileEntity(pos);
        SizedStorage storage = getEnumFromState(state);
        switch (storage) {
            case INCUBATOR: {
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
                    }
                } else return FULL_BLOCK_AABB;
            }
            default:
                return FULL_BLOCK_AABB;
        }
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack held, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (player.isSneaking()) return false;
        else if (held != null) {
            TileEntity tile = world.getTileEntity(pos);
            if (tile instanceof TileFillable) {
                return ((TileFillable)tile).onActivated(held);
            }
        }

        return false;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        SizedStorage storage = getEnumFromState(state);
        switch (storage) {
            case INCUBATOR:
                return new TileIncubator();
            default:
                return null;
        }
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileFillableSized tile = (TileFillableSized) world.getTileEntity(pos);
        boolean isFilled = tile.getFillAmount() > 0;
        IBlockState theState = isFilled ? state.withProperty(FILL, conversion.get(tile.getSize())) : state.withProperty(FILL, EMPTY);
        return super.getActualState(theState, world, pos);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModels(Item item, String name) {
        for (int i = 0; i < values.length; i++) {
            ModelLoader.setCustomModelResourceLocation(item, i, new ModelResourceLocation(getRegistryName(), "facing=north,fill=empty," + property.getName() + "=" + getEnumFromMeta(i).getName()));
        }
    }
}