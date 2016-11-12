package joshie.harvest.core.base.block;

import joshie.harvest.api.HFApi;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.base.item.ItemBlockHF;
import joshie.harvest.core.util.interfaces.ISellable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Locale;
import java.util.Random;

public abstract class BlockHFEnum<B extends BlockHFEnum, E extends Enum<E> & IStringSerializable> extends BlockHFBase<B> {
    protected static PropertyEnum<?> temporary;
    public final PropertyEnum<E> property;
    protected final Class<E> enumClass;
    protected final E[] values;

    //Main Constructor
    @SuppressWarnings("unchecked")
    public BlockHFEnum(Material material, Class<E> clazz, CreativeTabs tab) {
        super(preInit(material, clazz), tab);
        enumClass = clazz;
        property = (PropertyEnum<E>) temporary;
        values = clazz.getEnumConstants();
        setDefaultState(blockState.getBaseState());

        for (E e : values) {
            setHarvestLevel(getToolType(e), getToolLevel(e), getStateFromEnum(e));
        }
    }

    public void registerSellables(ItemBlockHF item) {
        if (values[0] instanceof ISellable) {
            for (E e: values) {
                long value = ((ISellable)e).getSellValue();
                if (value > 0L) {
                    HFApi.shipping.registerSellable(new ItemStack(item, 1, e.ordinal()), value);
                }
            }
        }
    }

    private static Material preInit(Material material, Class clazz) {
        temporary = PropertyEnum.create(clazz.getSimpleName().toLowerCase(Locale.ENGLISH), clazz);
        return material;
    }

    //Constructor default to farming tab
    public BlockHFEnum(Material material, Class<E> clazz) {
        this(material, clazz, HFTab.FARMING);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        if(property == null) return new BlockStateContainer(this, temporary);
        return new BlockStateContainer(this, property);
    }

    @SuppressWarnings("deprecation")
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
        if (meta < 0 || meta >= values.length) {
            meta = 0;
        }

        return values[meta];
    }

    public E getEnumFromStack(ItemStack stack) {
        return getEnumFromMeta(stack.getItemDamage());
    }

    public ItemStack getStackFromEnum(E e) {
        return new ItemStack(this, 1, e.ordinal());
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

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
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
    public String getUnlocalizedName(ItemStack stack) {
        return getEnumFromMeta(stack.getItemDamage()).name().toLowerCase(Locale.ENGLISH);
    }

    protected boolean shouldDisplayInCreative(E e) {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List<ItemStack> list) {
        for (E e : values) {
            if (shouldDisplayInCreative(e)) {
                list.add(new ItemStack(item, 1, e.ordinal()));
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public void registerModels(Item item, String name) {
        for (int i = 0; i < values.length; i++) {
            ModelLoader.setCustomModelResourceLocation(item, i, new ModelResourceLocation(getRegistryName(), property.getName() + "=" + getEnumFromMeta(i).getName()));
        }
    }
}