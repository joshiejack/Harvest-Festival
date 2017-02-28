package joshie.harvest.animals.block;

import joshie.harvest.animals.block.BlockTrough.Trough;
import joshie.harvest.animals.tile.TileTrough;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.animals.AnimalAction;
import joshie.harvest.api.animals.AnimalFoodType;
import joshie.harvest.api.animals.AnimalStats;
import joshie.harvest.api.animals.IAnimalFeeder;
import joshie.harvest.core.HFCore;
import joshie.harvest.core.base.block.BlockHFEnumRotatableMeta;
import joshie.harvest.core.base.tile.TileFillable;
import joshie.harvest.core.lib.CreativeSort;
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
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;

import static joshie.harvest.animals.block.BlockTrough.Trough.WOOD;
import static net.minecraft.util.EnumFacing.*;

public class BlockTrough extends BlockHFEnumRotatableMeta<BlockTrough, Trough> implements IAnimalFeeder {
    private static final AxisAlignedBB TROUGH_AABB =  new AxisAlignedBB(0D, 0D, 0D, 1D, 0.75D, 1D);
    public static final PropertyEnum<Section> SECTION = PropertyEnum.create("section", Section.class);

    public enum Trough implements IStringSerializable {
        WOOD;

        @Override
        public String getName() {
            return toString().toLowerCase(Locale.ENGLISH);
        }
    }

    public enum Section implements IStringSerializable {
        SINGLE, END, MIDDLE;

        @Override
        public String getName() {
            return toString().toLowerCase(Locale.ENGLISH);
        }
    }

    public BlockTrough() {
        super(Material.WOOD, Trough.class);
        setHardness(1.5F);
        setSoundType(SoundType.WOOD);
        setDefaultState(getDefaultState().withProperty(SECTION, Section.SINGLE));
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

    @SuppressWarnings("deprecation")
    @Override
    public void addCollisionBoxToList(IBlockState state, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull AxisAlignedBB entityBox, @Nonnull List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn) {
        if (entityIn instanceof EntityPlayer) addCollisionBoxToList(pos, entityBox, collidingBoxes, TROUGH_AABB);
        else addCollisionBoxToList(pos, entityBox, collidingBoxes, HFCore.FENCE_COLLISION);
    }

    @SuppressWarnings("deprecation")
    @Override
    @Nonnull
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
    @Nonnull
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        Trough trough = getEnumFromState(state);
        switch (trough) {
            case WOOD:
                return new TileTrough();
            default:
                return null;
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    @Nonnull
    public IBlockState getActualState(@Nonnull IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity tile = world instanceof ChunkCache ? ((ChunkCache)world).getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK) : world.getTileEntity(pos);
        if (tile instanceof TileTrough) {
            boolean north = isTrough(NORTH, world, pos);
            boolean south = isTrough(SOUTH, world, pos);
            if (north && !south) return state.withProperty(SECTION, Section.END).withProperty(FACING, EAST);
            if (south && !north) return state.withProperty(SECTION, Section.END).withProperty(FACING, WEST);
            if (south) return state.withProperty(SECTION, Section.MIDDLE).withProperty(FACING, EAST);

            boolean east = isTrough(EAST, world, pos);
            boolean west = isTrough(WEST, world, pos);
            if (west && east) return state.withProperty(SECTION, Section.MIDDLE).withProperty(FACING, SOUTH);
            if (east) return state.withProperty(SECTION, Section.END).withProperty(FACING, SOUTH);
            if (west) return state.withProperty(SECTION, Section.END).withProperty(FACING, NORTH);

            return state.withProperty(SECTION, Section.SINGLE);
        }

        return state;
    }

    @SuppressWarnings("ConstantConditions")
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
    @SuppressWarnings("ConstantConditions")
    public boolean feedAnimal(AnimalStats stats, World world, BlockPos pos, IBlockState state, boolean simulate) {
        if (HFApi.animals.canAnimalEatFoodType(stats, AnimalFoodType.GRASS)) {
            TileTrough master = ((TileTrough) world.getTileEntity(pos)).getMaster();
            if (master.getFillAmount() > 0) {
                if (simulate) return true;
                master.adjustFill(-1);
                stats.performAction(world, null, AnimalAction.FEED);
                //Good ol master block
                return true;
            }
        }

        return false;
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        super.onBlockPlacedBy(world, pos, state, placer, stack);
        if (getEnumFromState(state) == WOOD) {
            ((TileTrough)world.getTileEntity(pos)).onPlaced();
        }
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void breakBlock(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
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
            ModelLoader.setCustomModelResourceLocation(item, i, new ModelResourceLocation(getRegistryName(), "facing=north,section=single," + property.getName() + "=" + getEnumFromMeta(i).getName()));
        }
    }
}