package uk.joshiejack.penguinlib.block.base;

import uk.joshiejack.penguinlib.item.base.block.ItemBlockMulti;
import uk.joshiejack.penguinlib.block.interfaces.IPenguinBlock;
import uk.joshiejack.penguinlib.util.helpers.forge.RegistryHelper;
import uk.joshiejack.penguinlib.util.helpers.generic.ArrayHelper;
import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Locale;
import java.util.Random;

public abstract class BlockMultiGrowableBush<E extends Enum<E> & IStringSerializable> extends BlockBush implements IPenguinBlock, IGrowable {
    public static final PropertyInteger STAGE = PropertyInteger.create("stage", 0, 3);
    protected static PropertyEnum<?> temporary;
    public final PropertyEnum<E> property;
    protected final Class<E> enumClass;
    protected final E[] values;

    //Main Constructor
    @SuppressWarnings("unchecked")
    public BlockMultiGrowableBush(ResourceLocation registry, Class<E> clazz) {
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
        if (property == null) return new BlockStateContainer(this, temporary, STAGE);
        return new BlockStateContainer(this, property, STAGE);
    }

    @SuppressWarnings("deprecation")
    @Nonnull
    @Override
    public IBlockState getStateFromMeta(int data) {
        int meta = data %4;
        int stage = (int) Math.floor(data / 4);
        return getDefaultState().withProperty(property, getEnumFromMeta(meta)).withProperty(STAGE, stage);
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
    public int getMetaFromState(IBlockState state) {
        int meta = (state.getValue(property)).ordinal();
        int stage = state.getValue(STAGE);
        return meta + (stage * 4);
    }

    @Override
    public int damageDropped(IBlockState state) {
        return getMetaFromState(state);
    }

    @Override
    public void updateTick(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state, Random rand) {
        if (!worldIn.isRemote) {
            super.updateTick(worldIn, pos, state, rand);
            if (!worldIn.isAreaLoaded(pos, 1))
                return; // Forge: prevent loading unloaded chunks when checking neighbor's light
            grow(worldIn, rand, pos, state);
        }
    }

    @Override
    public boolean canGrow(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state, boolean isClient) {
        return state.getValue(STAGE) != 3;
    }

    @Override
    public boolean canUseBonemeal(@Nonnull World worldIn, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        return (double) worldIn.rand.nextFloat() < 0.45D;
    }

    @Override
    public void grow(@Nonnull World worldIn, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        if (state.getValue(STAGE) != 3) {
            worldIn.setBlockState(pos, state.cycleProperty(STAGE), 2);
        }
    }

    @Nonnull
    protected ItemStack getCreativeStack(E object) {
        return getStackFromEnum(object, 1);
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
            ModelLoader.setCustomModelResourceLocation(item, e.ordinal(), new ModelResourceLocation(getRegistryName(), property.getName() + "=" + e.getName() + ",stage=0"));
        }
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public ItemBlock createItemBlock() {
        return new ItemBlockMulti<>(getRegistryName(), enumClass, this);
    }
}
