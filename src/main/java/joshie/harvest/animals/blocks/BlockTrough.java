package joshie.harvest.animals.blocks;

import joshie.harvest.animals.blocks.BlockTrough.Trough;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.animals.AnimalFoodType;
import joshie.harvest.api.animals.IAnimalFeeder;
import joshie.harvest.api.animals.IAnimalTracked;
import joshie.harvest.blocks.tiles.TileFillable;
import joshie.harvest.core.lib.CreativeSort;
import joshie.harvest.core.base.BlockHFEnumRotatableMeta;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
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

import javax.annotation.Nullable;
import java.util.List;

import static joshie.harvest.animals.blocks.BlockTrough.Trough.WOOD;
import static net.minecraft.util.EnumFacing.*;

public class BlockTrough extends BlockHFEnumRotatableMeta<BlockTrough, Trough> implements IAnimalFeeder {
    private static final AxisAlignedBB TROUGH_AABB =  new AxisAlignedBB(0D, 0D, 0D, 1D, 0.75D, 1D);
    private static final AxisAlignedBB TROUGH_COLLISION =  new AxisAlignedBB(0D, 0D, 0D, 1D, 1.5D, 1D);

    public static final PropertyEnum<Section> SECTION = PropertyEnum.create("section", Section.class);

    public enum Trough implements IStringSerializable {
        WOOD;

        @Override
        public String getName() {
            return toString().toLowerCase();
        }
    }

    public enum Section implements IStringSerializable {
        SINGLE_EMPTY, SINGLE_FILLED, END_EMPTY, END_FILLED, MIDDLE_EMPTY, MIDDLE_FILLED;

        @Override
        public String getName() {
            return toString().toLowerCase();
        }
    }

    public BlockTrough() {
        super(Material.WOOD, Trough.class);
        setHardness(1.5F);
        setSoundType(SoundType.WOOD);
        setDefaultState(getDefaultState().withProperty(SECTION, Section.SINGLE_EMPTY));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        if(property == null) return new BlockStateContainer(this, temporary, FACING, SECTION);
        return new BlockStateContainer(this, property, FACING, SECTION);
    }

    @Override
    public String getToolType(Trough wood) {
        return "axe";
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn) {
        if (entityIn instanceof EntityPlayer) addCollisionBoxToList(pos, entityBox, collidingBoxes, TROUGH_AABB);
        else addCollisionBoxToList(pos, entityBox, collidingBoxes, TROUGH_COLLISION);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        return TROUGH_AABB;
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
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        Trough trough = getEnumFromState(state);
        switch (trough) {
            case WOOD:
                return new TileTrough();
            default:
                return null;
        }
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileTrough) {
            boolean isFilled = ((TileTrough)tile).getMaster().getFillAmount() > 0;
            boolean north = isTrough(NORTH, world, pos);
            boolean south = isTrough(SOUTH, world, pos);

            if (north && !south) return isFilled ? state.withProperty(SECTION, Section.END_FILLED).withProperty(FACING, EAST) : state.withProperty(SECTION, Section.END_EMPTY).withProperty(FACING, EAST);
            if (south && !north) return isFilled ? state.withProperty(SECTION, Section.END_FILLED).withProperty(FACING, WEST) : state.withProperty(SECTION, Section.END_EMPTY).withProperty(FACING, WEST);
            if (south && north) return isFilled ? state.withProperty(SECTION, Section.MIDDLE_FILLED).withProperty(FACING, EAST) : state.withProperty(SECTION, Section.MIDDLE_EMPTY).withProperty(FACING, EAST);

            boolean east = isTrough(EAST, world, pos);
            boolean west = isTrough(WEST, world, pos);
            if (west && east) return isFilled ? state.withProperty(SECTION, Section.MIDDLE_FILLED).withProperty(FACING, SOUTH) : state.withProperty(SECTION, Section.MIDDLE_EMPTY).withProperty(FACING, SOUTH);
            if (east && !west) return isFilled ? state.withProperty(SECTION, Section.END_FILLED).withProperty(FACING, SOUTH) : state.withProperty(SECTION, Section.END_EMPTY).withProperty(FACING, SOUTH);
            if (west && !east) return isFilled ? state.withProperty(SECTION, Section.END_FILLED).withProperty(FACING, NORTH) : state.withProperty(SECTION, Section.END_EMPTY).withProperty(FACING, NORTH);

            return isFilled ? state.withProperty(SECTION, Section.SINGLE_FILLED): state.withProperty(SECTION, Section.SINGLE_EMPTY);
        }

        return state;
    }

    private boolean isTrough(EnumFacing facing, IBlockAccess world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos.offset(facing));
        if (state.getBlock() == this) {
            if(getEnumFromState(state) == WOOD) {
                return (((TileTrough)world.getTileEntity(pos)).getMaster() == ((TileTrough)world.getTileEntity(pos.offset(facing))).getMaster());
            }
        }

        return false;
    }

    @Override
    public boolean feedAnimal(IAnimalTracked tracked, World world, BlockPos pos, IBlockState state) {
        if (HFApi.animals.canAnimalEatFoodType(tracked, AnimalFoodType.GRASS)) {
            TileTrough master = ((TileTrough) world.getTileEntity(pos)).getMaster();
            if (master.getFillAmount() > 0) {
                master.adjustFill(-1);
                //Good ol master block
                return true;
            }
        }

        return false;
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        super.onBlockPlacedBy(world, pos, state, placer, stack);
        if (getEnumFromState(state) == WOOD) {
            ((TileTrough)world.getTileEntity(pos)).onPlaced();
        }
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        if (getEnumFromState(state) == WOOD) {
            ((TileTrough)world.getTileEntity(pos)).onRemoved();
        }

        super.breakBlock(world, pos, state);
    }

    @Override
    public int getSortValue(ItemStack stack) {
        return CreativeSort.TROUGH;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModels(Item item, String name) {
        for (int i = 0; i < values.length; i++) {
            ModelLoader.setCustomModelResourceLocation(item, i, new ModelResourceLocation(getRegistryName(), "facing=north,section=single_filled," + property.getName() + "=" + getEnumFromMeta(i).getName()));
        }
    }
}