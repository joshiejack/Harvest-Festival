package uk.joshiejack.penguinlib.block.base;

import uk.joshiejack.penguinlib.item.base.block.ItemBlockMulti;
import uk.joshiejack.penguinlib.block.interfaces.IPenguinBlock;
import uk.joshiejack.penguinlib.util.interfaces.IPenguinMulti;
import uk.joshiejack.penguinlib.util.helpers.forge.RegistryHelper;
import uk.joshiejack.penguinlib.util.helpers.generic.ArrayHelper;
import net.minecraft.block.BlockBush;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Locale;

public abstract class BlockMultiBush<E extends Enum<E> & IStringSerializable> extends BlockBush implements IPenguinBlock, IPenguinMulti {
    protected static PropertyEnum<?> temporary;
    public final PropertyEnum<E> property;
    protected final Class<E> enumClass;
    protected final E[] values;

    //Main Constructor
    @SuppressWarnings("unchecked")
    public BlockMultiBush(ResourceLocation registry, Class<E> clazz) {
        super(preInit(Material.PLANTS, clazz));
        enumClass = clazz;
        property = (PropertyEnum<E>) temporary;
        values = clazz.getEnumConstants();
        setDefaultState(blockState.getBaseState());
        setSoundType(SoundType.PLANT);
        setHardness(0.0F);
        RegistryHelper.registerBlock(registry, this);
    }

    @SuppressWarnings("unchecked")
    private static Material preInit(Material material, Class clazz) {
        temporary = PropertyEnum.create(clazz.getSimpleName().toLowerCase(Locale.ENGLISH), clazz);
        return material;
    }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState() {
        if (property == null) return new BlockStateContainer(this, temporary);
        return new BlockStateContainer(this, property);
    }

    @SuppressWarnings("deprecation")
    @Nonnull
    @Override
    public IBlockState getStateFromMeta(int meta) {
       return getDefaultState().withProperty(property, getEnumFromMeta(meta));
    }

    public IBlockState getStateFromEnum(E e) {
        return getStateFromMeta(e.ordinal());
    }

    public E getEnumFromState(IBlockState state) {
        return state.getValue(property);
    }

    public E getEnumFromMeta(int meta) {
        return ArrayHelper.getArrayValue(values, meta);
    }

    public ItemStack getStackFromEnum(E e) {
        return getStackFromEnum(e, 1);
    }

    public ItemStack getStackFromEnum(E e, int amount) {
        return new ItemStack(this, amount, e.ordinal());
    }

    @Override
    public ItemStack getStackFromEnumString(String name, int count) {
        return getStackFromEnum(Enum.valueOf(enumClass, name.toUpperCase()), count);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return (state.getValue(property)).ordinal();
    }

    @Override
    public int damageDropped(IBlockState state) {
        return getMetaFromState(state);
    }

    @Nonnull
    protected ItemStack getCreativeStack(E object) {
        return getStackFromEnum(object, 1);
    }

    @Override
    public E[] getValues() {
        return values;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> items) {
        for (E e : values) {
            ItemStack stack = getCreativeStack(e);
            if (!stack.isEmpty()) {
                items.add(stack);
            }
        }
    }

    @SuppressWarnings("ConstantConditions")
    @SideOnly(Side.CLIENT)
    @Override
    public void registerModels(Item item) {
        for (E e : values) {
            ModelLoader.setCustomModelResourceLocation(item, e.ordinal(), new ModelResourceLocation(new ResourceLocation(getRegistryName().getNamespace(), getRegistryName().getPath() + "_" + e.getName()), "inventory"));
        }
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public ItemBlock createItemBlock() {
        return new ItemBlockMulti<>(getRegistryName(), enumClass, this);
    }
}
