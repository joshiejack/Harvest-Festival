package uk.joshiejack.penguinlib.block.base;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public class BlockRotatableDouble<E extends Enum<E> & IStringSerializable> extends BlockMultiRotatable<E> {
    public static final PropertyBool TOP = PropertyBool.create("top");

    public BlockRotatableDouble(ResourceLocation registry, Material material, Class<E> clazz) {
        super(registry, material, clazz);
        setDefaultState(getDefaultState().withProperty(TOP, false));
    }

    protected boolean isTop(IBlockState state) {
        return state.getValue(TOP);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        if(property == null) return new BlockStateContainer(this, temporary, FACING, TOP);
        return new BlockStateContainer(this, property, FACING, TOP);
    }

    @Nonnull
    @Override
    public IBlockState getStateFromMeta(int meta) {
        if (meta == 15) return getDefaultState().withProperty(TOP, true);
        return getDefaultState().withProperty(property, getEnumFromMeta(meta)).withProperty(FACING, getFacingFromMeta(meta));
    }

    private EnumFacing getFacingFromMeta(int meta) {
        return EnumFacing.values()[2 + ((meta / values.length) %4)];
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        if (isTop(state)) return 15;
        int enumValue = (state.getValue(property)).ordinal();
        int faceValue = (state.getValue(FACING)).ordinal();
        return enumValue + (values.length * enumValue) + faceValue;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean canRenderInLayer(IBlockState state, @Nonnull BlockRenderLayer layer) {
        return !isTop(state) && super.canRenderInLayer(state, layer);
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
        if (isTop(state)) world.setBlockToAir(pos.down());
        else world.setBlockToAir(pos.up());
        super.breakBlock(world, pos, state);
    }

    @Nonnull
    @Override
    public ItemStack getPickBlock(@Nonnull IBlockState state, RayTraceResult target, @Nonnull World world, @Nonnull BlockPos pos, EntityPlayer player) {
        return isTop(state) ? world.getBlockState(pos.down()).getBlock().getPickBlock(state, target, world, pos.down(), player) : super.getPickBlock(state, target, world, pos, player);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModels(Item item) {
        ModelLoader.setCustomStateMapper(this, new StateMap.Builder().ignore(TOP).build());
        super.registerModels(item);
    }
}
