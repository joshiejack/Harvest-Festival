package uk.joshiejack.penguinlib.block.base;

import com.google.common.collect.Lists;
import uk.joshiejack.penguinlib.item.base.block.ItemBlockMultiLeaves;
import uk.joshiejack.penguinlib.block.interfaces.IPenguinBlock;
import uk.joshiejack.penguinlib.util.helpers.forge.RegistryHelper;
import uk.joshiejack.penguinlib.util.helpers.generic.ArrayHelper;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public abstract class BlockMultiLeaves<E extends Enum<E> & IStringSerializable> extends BlockLeaves implements IShearable, IPenguinBlock {
    protected static PropertyEnum<?> temporary;
    public final PropertyEnum<E> property;
    protected final Class<E> enumClass;
    protected final E[] values;

    //Main Constructor
    public BlockMultiLeaves(ResourceLocation registry, Class<E> clazz) {
        this(registry, preInit(clazz), true);
    }

    @SuppressWarnings("unchecked")
    private BlockMultiLeaves(ResourceLocation registry, Class<E> clazz, boolean bool) {
        enumClass = clazz;
        property = (PropertyEnum<E>) temporary;
        values = clazz.getEnumConstants();
        setDefaultState(blockState.getBaseState());
        RegistryHelper.registerBlock(registry, this);
    }

    @SuppressWarnings("unchecked")
    private static <E> Class<E> preInit(Class e) {
        temporary = PropertyEnum.create(e.getSimpleName().toLowerCase(Locale.ENGLISH), e);
        return e;
    }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState() {
        if(property == null) return new BlockStateContainer(this, temporary, CHECK_DECAY, DECAYABLE);
        return new BlockStateContainer(this, property, CHECK_DECAY, DECAYABLE);
    }

    public E getEnumFromMeta(int meta) {
        return ArrayHelper.getArrayValue(values, meta);
    }

    @Nonnull
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(property, getEnumFromMeta(meta)).withProperty(DECAYABLE, (meta & 4) == 0).withProperty(CHECK_DECAY, (meta & 8) > 0);
    }

    protected E getEnumFromState(IBlockState state) {
        return state.getValue(property);
    }

    public IBlockState getStateFromEnum(E e) {
        return getStateFromMeta(e.ordinal());
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Nonnull
    @SideOnly(Side.CLIENT)
    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int i = 0;
        i = i | (state.getValue(property)).ordinal();

        if (!(state.getValue(DECAYABLE))) {
            i |= 4;
        }

        if (state.getValue(CHECK_DECAY)) {
            i |= 8;
        }

        return i;
    }

    @Override
    public ItemBlock createItemBlock() {
        return new ItemBlockMultiLeaves<>(Objects.requireNonNull(getRegistryName()), enumClass, this);
    }

    @Nonnull
    @Override
    public BlockPlanks.EnumType getWoodType(int meta) {
        return BlockPlanks.EnumType.OAK;
    }

    @Nonnull
    @Override
    public List<ItemStack> onSheared(@Nonnull ItemStack item, IBlockAccess world, BlockPos pos, int fortune) {
        return Lists.newArrayList(new ItemStack(this, 1, world.getBlockState(pos).getValue(property).ordinal()));
    }

    @SuppressWarnings("ConstantConditions")
    @SideOnly(Side.CLIENT)
    @Override
    public void registerModels(Item item) {
        ModelLoader.setCustomStateMapper(this, new StateMap.Builder().ignore(CHECK_DECAY, DECAYABLE).build());
        for (E e: values) {
            ModelLoader.setCustomModelResourceLocation(item, e.ordinal(), new ModelResourceLocation(getRegistryName(), property.getName() + "=" + e.getName()));
        }
    }
}
