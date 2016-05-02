package joshie.harvest.core.util.base;

import joshie.harvest.core.HFTab;
import joshie.harvest.core.util.generic.IHasMetaBlock;
import joshie.harvest.core.util.generic.IHasMetaItem;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;

public abstract class BlockHFBaseMeta<E extends Enum<E> & IStringSerializable> extends BlockHFBase implements IHasMetaBlock {
    private static PropertyEnum<?> temporary;
    public final PropertyEnum<E> property;
    private final E[] values;

    //Main Constructor
    public BlockHFBaseMeta(Material material, Class<E> clazz, CreativeTabs tab) {
        super(preInit(material, clazz), tab);
        property = (PropertyEnum<E>) temporary;
        values = clazz.getEnumConstants();
        setDefaultState(blockState.getBaseState());

        for (E e : values) {
            setHarvestLevel(getToolType(e), getToolLevel(e), getStateFromEnum(e));
        }
    }

    private static Material preInit(Material material, PropertyEnum<?> property) {
        temporary = property;
        return material;
    }

    private static Material preInit(Material material, Class clazz) {
        temporary = PropertyEnum.create(clazz.getSimpleName().toLowerCase(), clazz);
        return material;
    }

    //Constructor default to farming tab
    public BlockHFBaseMeta(Material material, Class<E> clazz) {
        this(material, clazz, HFTab.FARMING);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        if(property == null)return new BlockStateContainer(this, temporary);
        return new BlockStateContainer(this, property);
    }

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
    public Class<? extends ItemBlock> getItemClass() {
        return null;
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int side) {
        return !doesDrop(state) ? null : super.getItemDropped(state, rand, side);
    }

    protected boolean doesDrop(IBlockState state) {
        return true;
    }

    protected String getName(int i) {
        return ((IHasMetaItem) Item.getItemFromBlock(this)).getName(new ItemStack(this, 1, i));
    }

    protected boolean isValidTab(CreativeTabs tab, E e) {
        return tab == getCreativeTabToDisplayOn();
    }

    public boolean isActive(E e) {
        return true;
    }

    @Override
    public BlockHFBaseMeta setUnlocalizedName(String name) {
        super.setUnlocalizedName(name);
        return this;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List<ItemStack> list) {
        for (E e : values) {
            if (isActive(e) && isValidTab(tab, e)) {
                list.add(new ItemStack(item, 1, e.ordinal()));
            }
        }
    }
}