package joshie.harvest.core.base.block;

import joshie.harvest.core.HFTab;
import joshie.harvest.core.helpers.generic.DirectionHelper;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class BlockHFEnumRotatableMeta<B extends BlockHFEnumRotatableMeta, E extends Enum<E> & IStringSerializable> extends BlockHFEnum<B, E> {
    protected static final PropertyDirection FACING = BlockHorizontal.FACING;
    //Main Constructor
    public BlockHFEnumRotatableMeta(Material material, Class<E> clazz, CreativeTabs tab) {
        super(material, clazz, tab);
        setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
    }

    //Constructor default to farming tab
    public BlockHFEnumRotatableMeta(Material material, Class<E> clazz) {
        this(material, clazz, HFTab.FARMING);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        if(property == null) return new BlockStateContainer(this, temporary, FACING);
        return new BlockStateContainer(this, property, FACING);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(property, getEnumFromMeta(meta)).withProperty(FACING, getFacingFromMeta(meta));
    }

    public EnumFacing getFacingFromMeta(int meta) {
        return EnumFacing.values()[2 + ((meta / values.length) %4)];
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int enumValue = (state.getValue(property)).ordinal();
        int faceValue = (state.getValue(FACING)).ordinal();
        return enumValue + (values.length * enumValue) + faceValue;
    }

    @Override
    public int damageDropped(IBlockState state) {
        return (state.getValue(property)).ordinal(); //Return without the extra data
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        EnumFacing facing = DirectionHelper.getFacingFromEntity(placer);
        world.setBlockState(pos, state.withProperty(FACING, facing));
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState withRotation(IBlockState state, Rotation rot) {
        return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
        return state.withRotation(mirrorIn.toRotation(state.getValue(FACING)));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModels(Item item, String name) {
        for (int i = 0; i < values.length; i++) {
            if (Character.toLowerCase(property.getName().charAt(0)) < Character.toLowerCase('f')) {
                ModelLoader.setCustomModelResourceLocation(item, i, new ModelResourceLocation(getRegistryName(), property.getName() + "=" + getEnumFromMeta(i).getName() + ",facing=north"));
            } else {
                ModelLoader.setCustomModelResourceLocation(item, i, new ModelResourceLocation(getRegistryName(), "facing=north," + property.getName() + "=" + getEnumFromMeta(i).getName()));
            }
        }
    }
}