package joshie.harvest.animals.block;

import joshie.harvest.animals.block.BlockSizedStorage.SizedStorage;
import joshie.harvest.animals.tile.TileIncubator;
import joshie.harvest.api.core.ISizeable.Size;
import joshie.harvest.core.base.block.BlockHFEnumRotatableTile;
import joshie.harvest.core.base.tile.TileFillableSized;
import joshie.harvest.core.util.IFaceable;
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

import java.util.Locale;

import static joshie.harvest.animals.block.BlockSizedStorage.Fill.EMPTY;

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
            return toString().toLowerCase(Locale.ENGLISH);
        }
    }

    public enum Fill implements IStringSerializable {
        EMPTY, SMALL, MEDIUM, LARGE;

        public static Fill getFillFromSize(Size size) {
            return size == Size.SMALL ? SMALL : size == Size.MEDIUM ? MEDIUM : LARGE;
        }

        @Override
        public String getName() {
            return toString().toLowerCase(Locale.ENGLISH);
        }
    }

    public BlockSizedStorage() {
        super(Material.WOOD, SizedStorage.class);
        setHardness(1.5F);
        setSoundType(SoundType.WOOD);
        setDefaultState(getDefaultState().withProperty(FILL, EMPTY));
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

    @SuppressWarnings("deprecation")
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof IFaceable) {
            switch (((IFaceable)tile).getFacing()) {
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
        }

        return FULL_BLOCK_AABB;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack held, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (player.isSneaking()) return false;
        else if (held != null) {
            TileEntity tile = world.getTileEntity(pos);
            if (tile instanceof TileFillableSized) {
                return ((TileFillableSized)tile).onActivated(player, held);
            }
        }

        return false;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileIncubator();
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileFillableSized tile = (TileFillableSized) world.getTileEntity(pos);
        boolean isFilled = tile.getFillAmount() > 0;
        IBlockState theState = isFilled ? state.withProperty(FILL, Fill.getFillFromSize(tile.getSize())) : state.withProperty(FILL, EMPTY);
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