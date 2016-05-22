package joshie.harvest.blocks;

import joshie.harvest.api.animals.IAnimalFeeder;
import joshie.harvest.api.animals.IAnimalTracked;
import joshie.harvest.blocks.BlockWood.Woodware;
import joshie.harvest.blocks.tiles.TileFillable;
import joshie.harvest.blocks.tiles.TileNest;
import joshie.harvest.blocks.tiles.TileTrough;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.lib.CreativeSort;
import joshie.harvest.core.util.base.BlockHFBaseEnumRotatableMeta;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import static joshie.harvest.api.HFApi.shipping;
import static joshie.harvest.blocks.BlockWood.Woodware.SHIPPING;
import static joshie.harvest.blocks.BlockWood.Woodware.TROUGH;
import static net.minecraft.util.EnumFacing.*;

public class BlockWood extends BlockHFBaseEnumRotatableMeta<Woodware> implements IAnimalFeeder {
    private static final AxisAlignedBB SHIPPING_AABB = new AxisAlignedBB(0D, 0D, 0D, 1D, 0.6D, 1D);
    public static final PropertyEnum<Type> TYPE = PropertyEnum.create("type", Type.class);

    public enum Woodware implements IStringSerializable {
        SHIPPING, NEST, TROUGH;

        @Override
        public String getName() {
            return toString().toLowerCase();
        }
    }

    public enum Type implements IStringSerializable {
        NORMAL, FILLED, END_EMPTY, END_FILLED, MIDDLE_EMPTY, MIDDLE_FILLED;

        @Override
        public String getName() {
            return toString().toLowerCase();
        }
    }

    public BlockWood() {
        super(Material.WOOD, Woodware.class);
        setHardness(1.5F);
        setSoundType(SoundType.WOOD);
        setDefaultState(getDefaultState().withProperty(TYPE, Type.NORMAL));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        if(property == null) return new BlockStateContainer(this, temporary, FACING, TYPE);
        return new BlockStateContainer(this, property, FACING, TYPE);
    }

    @Override
    public String getToolType(Woodware wood) {
        return "axe";
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        Woodware wood = getEnumFromState(state);
        switch (wood) {
            case SHIPPING:
                return SHIPPING_AABB;
            default:
                return FULL_BLOCK_AABB;
        }
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack held, EnumFacing side, float hitX, float hitY, float hitZ) {
        Woodware wood = getEnumFromState(state);
        if (player.isSneaking()) return false;
        else if ((wood == SHIPPING) && held != null) {
            long sell = shipping.getSellValue(held);
            if (sell > 0) {
                if (!world.isRemote) {
                    HFTrackers.getServerPlayerTracker(player).getTracking().addForShipping(held.copy());
                }

                held.splitStack(1);
                return true;
            }
        } else if (held != null) {
            TileEntity tile = world.getTileEntity(pos);
            if (tile instanceof TileFillable) {
                return ((TileFillable)tile).onActivated(held);
            }
        }

        return false;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return getEnumFromState(state) != SHIPPING;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        Woodware woodware = getEnumFromState(state);
        switch (woodware) {
            case NEST:
                return new TileNest();
            case TROUGH:
                return new TileTrough();
            default:
                return null;
        }
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileFillable) {

            if (tile instanceof TileTrough) {
                boolean isFilled = ((TileTrough)tile).getMaster().getFillAmount() > 0;
                boolean north = isTrough(NORTH, world, pos);
                boolean east = isTrough(EAST, world, pos);
                boolean south = isTrough(SOUTH, world, pos);
                boolean west = isTrough(WEST, world, pos);
                if (north && !south) return isFilled ? state.withProperty(TYPE, Type.END_FILLED).withProperty(FACING, EAST) : state.withProperty(TYPE, Type.END_EMPTY).withProperty(FACING, EAST);
                if (south && !north) return isFilled ? state.withProperty(TYPE, Type.END_FILLED).withProperty(FACING, WEST) : state.withProperty(TYPE, Type.END_EMPTY).withProperty(FACING, WEST);
                if (south && north) return isFilled ? state.withProperty(TYPE, Type.MIDDLE_FILLED).withProperty(FACING, EAST) : state.withProperty(TYPE, Type.MIDDLE_EMPTY).withProperty(FACING, EAST);
                if (west && east) return isFilled ? state.withProperty(TYPE, Type.MIDDLE_FILLED).withProperty(FACING, SOUTH) : state.withProperty(TYPE, Type.MIDDLE_EMPTY).withProperty(FACING, SOUTH);
                if (east && !west) return isFilled ? state.withProperty(TYPE, Type.END_FILLED).withProperty(FACING, SOUTH) : state.withProperty(TYPE, Type.END_EMPTY).withProperty(FACING, SOUTH);
                if (west && !east) return isFilled ? state.withProperty(TYPE, Type.END_FILLED).withProperty(FACING, NORTH) : state.withProperty(TYPE, Type.END_EMPTY).withProperty(FACING, NORTH);
            }

            boolean isFilled = ((TileFillable)tile).getFillAmount() > 0;
            return isFilled ? state.withProperty(TYPE, Type.FILLED) : state.withProperty(TYPE, Type.NORMAL);
        }

        return state;
    }

    private boolean isTrough(EnumFacing facing, IBlockAccess world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos.offset(facing));
        if (state.getBlock() == this) {
            if(getEnumFromState(state) == TROUGH) {
                return (((TileTrough)world.getTileEntity(pos)).getMaster() == ((TileTrough)world.getTileEntity(pos.offset(facing))).getMaster());
            }
        }

        return false;
    }

    @Override
    public boolean canFeedAnimal(IAnimalTracked tracked, World world, BlockPos pos, IBlockState state) {
        if (getEnumFromState(state) == TROUGH) {
            TileTrough master = ((TileTrough)world.getTileEntity(pos)).getMaster();
            if (master.getFillAmount() > 0) {
                master.add(-1);
                //Good ol master block
                return true;
            }
        }

        return false;
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        super.onBlockPlacedBy(world, pos, state, placer, stack);
        if (getEnumFromState(state) == TROUGH) {
            ((TileTrough)world.getTileEntity(pos)).onPlaced();
        }
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        if (getEnumFromState(state) == TROUGH) {
            ((TileTrough)world.getTileEntity(pos)).onRemoved();
        }

        super.breakBlock(world, pos, state);
    }

    @Override
    public boolean isValidTab(CreativeTabs tab, Woodware wood) {
        return tab == HFTab.FARMING;
    }

    @Override
    public int getSortValue(ItemStack stack) {
        return CreativeSort.TROUGH;
    }
}