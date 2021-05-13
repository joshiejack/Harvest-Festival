package uk.joshiejack.penguinlib.block.base;

import uk.joshiejack.penguinlib.item.base.block.ItemBlockMulti;
import uk.joshiejack.penguinlib.block.interfaces.IPenguinBlock;
import uk.joshiejack.penguinlib.util.interfaces.IPenguinMulti;
import uk.joshiejack.penguinlib.util.helpers.forge.RegistryHelper;
import uk.joshiejack.penguinlib.util.helpers.generic.ArrayHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Locale;
import java.util.Random;

public abstract class BlockMulti<E extends Enum<E> & IStringSerializable> extends Block implements IPenguinBlock, IPenguinMulti<E> {
    protected static PropertyEnum<?> temporary;
    public final PropertyEnum<E> property;
    protected final Class<E> enumClass;
    protected final E[] values;

    //Main Constructor
    @SuppressWarnings("unchecked")
    public BlockMulti(ResourceLocation registry, Material material, Class<E> clazz) {
        super(preInit(material, clazz));
        enumClass = clazz;
        property = (PropertyEnum<E>) temporary;
        values = clazz.getEnumConstants();
        setDefaultState(blockState.getBaseState());

        for (E e : values) {
            setHarvestLevel(getToolType(e), getToolLevel(e), getStateFromEnum(e));
        }

        RegistryHelper.registerBlock(registry, this);
    }

    @SuppressWarnings("unchecked")
    private static Material preInit(Material material, Class clazz) {
        String name = clazz.getSimpleName().toLowerCase(Locale.ENGLISH).replace("enum", "");
        temporary = PropertyEnum.create(name, clazz);
        return material;
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState() {
        if(property == null) return new BlockStateContainer(this, temporary);
        return new BlockStateContainer(this, property);
    }

    @SuppressWarnings("deprecation")
    @Nonnull
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(property, getEnumFromMeta(meta));
    }

    public IBlockState getStateFromEnum(E e) {
        return getDefaultState().withProperty(property, e);
    }

    public E getEnumFromBlockPos(IBlockAccess world, BlockPos pos) {
        return getEnumFromState(world.getBlockState(pos));
    }

    public E getEnumFromState(IBlockState state) {
        return state.getValue(property);
    }

    public E getEnumFromMeta(int meta) {
        return ArrayHelper.getArrayValue(values, meta);
    }

    public E getEnumFromStack(ItemStack stack) {
        return getEnumFromMeta(stack.getItemDamage());
    }

    public ItemStack getStackFromEnum(E e) {
        return getStackFromEnum(e, 1);
    }

    public ItemStack getStackFromEnum(E e, int amount) {
        return new ItemStack(this, amount, e.ordinal());
    }

    public ItemStack getStackFromEnumString(String name) {
        return getStackFromEnum(Enum.valueOf(enumClass, name.toUpperCase()));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return (state.getValue(property)).ordinal();
    }

    @Override
    public int damageDropped(IBlockState state) {
        return getMetaFromState(state);
    }

    //Default to pickaxe
    public String getToolType(E type) {
        return "pickaxe";
    }

    //Default to level 0
    protected int getToolLevel(E level) {
        return 0;
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int side) {
        return !doesDrop(state) ? null : super.getItemDropped(state, rand, side);
    }

    protected boolean doesDrop(IBlockState state) {
        return true;
    }

    @SideOnly(Side.CLIENT)
    @Nonnull
    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isFullCube(IBlockState blockState) {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isOpaqueCube(IBlockState blockState) {
        return false;
    }

    @Override
    public ItemStack getStackFromEnumString(String name, int size) {
        return getStackFromEnum(Enum.valueOf(enumClass, name.toUpperCase()), size);
    }

    @Override
    public E[] getValues() {
        return values;
    }

    @Nonnull
    protected ItemStack getCreativeStack(E object) {
        return getStackFromEnum(object, 1);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public ItemBlock createItemBlock() {
        return new ItemBlockMulti<>(getRegistryName(), enumClass, this);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> items) {
        for (E e: values) {
            ItemStack stack = getCreativeStack(e);
            if (!stack.isEmpty()) {
                items.add(stack);
            }
        }
    }

    @SuppressWarnings("ConstantConditions")
    @SideOnly(Side.CLIENT)
    protected void registerModel(Item item, E e) {
        ModelLoader.setCustomModelResourceLocation(item, e.ordinal(), new ModelResourceLocation(getRegistryName(), property.getName() + "=" + e.getName()));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModels(Item item) {
        for (E e: values) {
            registerModel(item, e);
        }
    }
}
