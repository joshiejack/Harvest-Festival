package uk.joshiejack.penguinlib.block.base;

import uk.joshiejack.penguinlib.util.helpers.minecraft.EntityHelper;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public class BlockMultiRotatable<E extends Enum<E> & IStringSerializable> extends BlockMulti<E> {
    public static final PropertyDirection FACING = BlockHorizontal.FACING;

    public BlockMultiRotatable(ResourceLocation registry, Material material, Class<E> clazz) {
        super(registry, material, clazz);
        setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        if(property == null) return new BlockStateContainer(this, temporary, FACING);
        return new BlockStateContainer(this, property, FACING);
    }

    public IBlockState withFacing(IBlockState state, EnumFacing facing) {
        return state.withProperty(FACING, facing);
    }

    @SuppressWarnings("deprecation")
    @Nonnull
    @Override
    public IBlockState withRotation(@Nonnull IBlockState state, Rotation rot) {
        return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
    }

    @SuppressWarnings("deprecation")
    @Nonnull
    @Override
    public IBlockState withMirror(@Nonnull IBlockState state, Mirror mirrorIn) {
        return state.withRotation(mirrorIn.toRotation(state.getValue(FACING)));
    }

    @Nonnull
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(property, getEnumFromMeta(meta)).withProperty(FACING, getFacingFromMeta(meta));
    }

    @Override
    public E getEnumFromMeta(int meta) {
        return super.getEnumFromMeta(meta % values.length);
    }

    private EnumFacing getFacingFromMeta(int meta) {
        return EnumFacing.byHorizontalIndex(meta / values.length);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int enumValue = (state.getValue(property)).ordinal();
        int faceValue = (state.getValue(FACING).getHorizontalIndex());
        return enumValue + (faceValue * values.length);
    }

    @Override
    public int damageDropped(IBlockState state) {
        return (state.getValue(property)).ordinal(); //Return without the extra data
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        EnumFacing facing = EntityHelper.getFacingFromEntity(placer);
        world.setBlockState(pos, state.withProperty(FACING, facing));
    }

    @SuppressWarnings("ConstantConditions")
    @SideOnly(Side.CLIENT)
    @Override
    public void registerModels(Item item) {
        for (int i = 0; i < values.length; i++) {
            if (Character.toLowerCase(property.getName().charAt(0)) < Character.toLowerCase('f')) {
                ModelLoader.setCustomModelResourceLocation(item, i, new ModelResourceLocation(getRegistryName(), property.getName() + "=" + getEnumFromMeta(i).getName() + ",facing=north"));
            } else {
                ModelLoader.setCustomModelResourceLocation(item, i, new ModelResourceLocation(getRegistryName(), "facing=north," + property.getName() + "=" + getEnumFromMeta(i).getName()));
            }
        }
    }
}
