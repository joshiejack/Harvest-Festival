package uk.joshiejack.penguinlib.block.base;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Random;


public abstract class BlockMultiTileRotatableDouble<E extends Enum<E> & IStringSerializable> extends BlockMultiTileRotatable<E> {
    private static final AxisAlignedBB DOUBLE_AABB = new AxisAlignedBB(0F, 0F, 0F, 1F, 2F, 1F);
    private static final AxisAlignedBB TOP_AABB = new AxisAlignedBB(0F, -1F, 0F, 1F, 1F, 1F);
    private static final PropertyBool TOP = PropertyBool.create("top");

    //MAXIMUM 15 items....
    public BlockMultiTileRotatableDouble(ResourceLocation registry, Material material, Class<E> clazz) {
        super(registry, material, clazz);
        setDefaultState(getDefaultState().withProperty(TOP, false));
    }

    @SuppressWarnings("deprecation")
    @Nonnull
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        return isTop(state) ? TOP_AABB : DOUBLE_AABB;
    }

    protected boolean isTop(IBlockState state) {
        return state.getValue(TOP);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean canRenderInLayer(IBlockState state, @Nonnull BlockRenderLayer layer) {
        return !isTop(state) && super.canRenderInLayer(state, layer);
    }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState() {
        if(property == null) return new BlockStateContainer(this, temporary, FACING, TOP);
        return new BlockStateContainer(this, property, FACING, TOP);
    }

    @SuppressWarnings("deprecation")
    @Nonnull
    @Override
    public IBlockState getStateFromMeta(int meta) {
        if (meta == 15) return getDefaultState().withProperty(TOP, true);
        else return getDefaultState().withProperty(property, getEnumFromMeta(meta)).withProperty(TOP, false);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return state.getValue(TOP) ? Items.AIR : super.getItemDropped(state, rand, fortune);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return isTop(state) ? 15: state.getValue(property).ordinal();
    }

    @SuppressWarnings("deprecation")
    @SideOnly(Side.CLIENT)
    @Override
    public boolean shouldSideBeRendered(IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, EnumFacing side) {
        if (!isTop(state)) {
            AxisAlignedBB axisalignedbb = state.getBoundingBox(world, pos);
            switch (side) {
                case DOWN:
                    if (axisalignedbb.minY > 0.0D) {
                        return true;
                    }

                    break;
                case UP:
                    if (axisalignedbb.maxY < 1.0D) {
                        return true;
                    }

                    break;
                case NORTH:
                    if (axisalignedbb.minZ > 0.0D) {
                        return true;
                    }

                    break;
                case SOUTH:
                    if (axisalignedbb.maxZ < 1.0D) {
                        return true;
                    }

                    break;
                case WEST:
                    if (axisalignedbb.minX > 0.0D) {
                        return true;
                    }

                    break;
                case EAST:
                    if (axisalignedbb.maxX < 1.0D)  {
                        return true;
                    }
            }

            return !(world.getBlockState(pos.offset(side)).doesSideBlockRendering(world, pos.offset(side), side.getOpposite()) && world.getBlockState(pos.offset(side).up()).doesSideBlockRendering(world, pos.offset(side).up(), side.getOpposite()));
        } else return super.shouldSideBeRendered(state, world, pos, side);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (isTop(state)) return super.onBlockActivated(world, pos.down(), world.getBlockState(pos.down()), player, hand, side, hitX, hitY, hitZ);
        else return super.onBlockActivated(world, pos, state, player, hand, side, hitX, hitY, hitZ);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        worldIn.setBlockState(pos.up(), getDefaultState().withProperty(TOP, true), 2);
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
    }

    @Override
    public void breakBlock(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        if (isTop(state)) world.destroyBlock(pos.down(), true);
        else world.setBlockToAir(pos.up());
        super.breakBlock(world, pos, state);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return !isTop(state);
    }

    @Nonnull
    @Override
    public ItemStack getPickBlock(@Nonnull IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        IBlockState down = world.getBlockState(pos.down());
        return isTop(state) ? down.getBlock().getPickBlock(down, target, world, pos.down(), player) : super.getPickBlock(state, target, world, pos, player);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModels(Item item) {
        ModelLoader.setCustomStateMapper(this, new StateMap.Builder().ignore(TOP).build());
        super.registerModels(item);
    }
}
