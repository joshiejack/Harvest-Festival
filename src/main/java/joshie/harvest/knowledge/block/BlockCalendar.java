package joshie.harvest.knowledge.block;

import joshie.harvest.HarvestFestival;
import joshie.harvest.core.base.block.BlockHFBase;
import joshie.harvest.core.handlers.GuiHandler;
import joshie.harvest.knowledge.HFKnowledge;
import joshie.harvest.knowledge.item.ItemBook;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import static joshie.harvest.core.lib.HFModInfo.MODID;

public class BlockCalendar extends BlockHFBase<BlockCalendar> {
    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    private static final AxisAlignedBB EAST_AABB = new AxisAlignedBB(0.0D, 0.07125D, 0.0D, 0.125D, 0.94125D, 1.0D);
    private static final AxisAlignedBB WEST_AABB = new AxisAlignedBB(0.875D, 0.07125D, 0.0D, 1.0D, 0.94125D, 1.0D);
    private static final AxisAlignedBB SOUTH_AABB = new AxisAlignedBB(0.0D, 0.07125D, 0.0D, 1.0D, 0.94125D, 0.125D);
    private static final AxisAlignedBB NORTH_AABB = new AxisAlignedBB(0.0D, 0.07125D, 0.875D, 1.0D, 0.94125D, 1.0D);

    public BlockCalendar() {
        super(Material.CLOTH, null);
        setSoundType(SoundType.CLOTH);
        setHardness(0.2F);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!player.isSneaking()) {
            player.openGui(HarvestFestival.instance, GuiHandler.CALENDAR_GUI, world, 0, 0, 0);
            return true;
        }
        return false;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
        return true;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean canSpawnInBlock() {
        return true;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        return NULL_AABB;
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        switch (state.getValue(FACING)) {
            case NORTH:
            default:
                return NORTH_AABB;
            case SOUTH:
                return SOUTH_AABB;
            case WEST:
                return WEST_AABB;
            case EAST:
                return EAST_AABB;
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos changedBlockPos) {
        EnumFacing facing = state.getValue(FACING);

        if (!world.getBlockState(pos.offset(facing.getOpposite())).getMaterial().isSolid()) {
            this.dropBlockAsItem(world, pos, state, 0);
            world.setBlockToAir(pos);
        }
        super.neighborChanged(state, world, pos, block, changedBlockPos);
    }

    @Override
    @Nonnull
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return HFKnowledge.BOOK.getStackFromEnum(ItemBook.Book.CALENDAR);
    }

    @Override
    @Nonnull
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        List<ItemStack> ret = new ArrayList<>();
        ret.add(HFKnowledge.BOOK.getStackFromEnum(ItemBook.Book.CALENDAR));
        return ret;
    }

    @Override
    public boolean canPlaceBlockAt(World world, BlockPos pos) {
        return !this.hasInvalidNeighbor(world, pos) && super.canPlaceBlockAt(world, pos);
    }

    protected boolean hasInvalidNeighbor(World world, BlockPos pos) {
        return this.isInvalidNeighbor(world, pos, EnumFacing.NORTH) || this.isInvalidNeighbor(world, pos, EnumFacing.SOUTH) || this.isInvalidNeighbor(world, pos, EnumFacing.WEST) || this.isInvalidNeighbor(world, pos, EnumFacing.EAST);
    }

    protected boolean isInvalidNeighbor(World world, BlockPos pos, EnumFacing facing) {
        return world.getBlockState(pos.offset(facing)).getMaterial() == Material.CACTUS;
    }

    @Override
    public BlockCalendar register(String name) {
        setUnlocalizedName(name.replace("_", "."));
        setRegistryName(new ResourceLocation(MODID, name));
        GameRegistry.register(this);
        return this;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> list) {}

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    public IBlockState getStateFromMeta(int meta) {
        EnumFacing facing = EnumFacing.getFront(meta);

        if (facing.getAxis() == EnumFacing.Axis.Y) {
            facing = EnumFacing.NORTH;
        }
        return this.getDefaultState().withProperty(FACING, facing);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getIndex();
    }

    @Override
    @Nonnull
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
    }
}