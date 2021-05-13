package uk.joshiejack.penguinlib.item.base;

import uk.joshiejack.penguinlib.item.interfaces.IPenguinItem;
import uk.joshiejack.penguinlib.util.interfaces.IPenguinMulti;
import uk.joshiejack.penguinlib.util.helpers.forge.RegistryHelper;
import uk.joshiejack.penguinlib.util.helpers.generic.ArrayHelper;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Locale;
import java.util.Objects;

public abstract class ItemMultiPlantable<E extends Enum<E>> extends ItemSeeds implements IPenguinItem, IPenguinMulti {
    private final Class<E> enumClass;
    private final E[] values;
    protected final String prefix;

    public ItemMultiPlantable(ResourceLocation registry, Class<E> clazz) {
        super(Blocks.FARMLAND, Blocks.FARMLAND);
        enumClass = clazz;
        values = clazz.getEnumConstants();
        prefix = getPrefixFromRegistryName(registry);
        setHasSubtypes(true); //metadata
        RegistryHelper.setRegistryAndLocalizedName(registry, this);
    }

    @Override
    public Enum[] getValues() {
        return values;
    }

    protected String getPrefixFromRegistryName(ResourceLocation registry) {
        return registry.getNamespace() + ".item." + registry.getPath() + ".";
    }

    protected E getEnumFromStack(ItemStack stack) {
        return getEnumFromMeta(stack.getItemDamage());
    }

    private E getEnumFromMeta(int meta) {
        return ArrayHelper.getArrayValue(values, meta);
    }

    public ItemStack getStackFromEnum(E e, int size) {
        return new ItemStack(this, size, e.ordinal());
    }

    @Override
    public ItemStack getStackFromEnumString(String name, int size) {
        return getStackFromEnum(Enum.valueOf(enumClass, name.toUpperCase()), size);
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack itemstack = player.getHeldItem(hand);
        net.minecraft.block.state.IBlockState state = worldIn.getBlockState(pos);
        if (facing == EnumFacing.UP && player.canPlayerEdit(pos.offset(facing), facing, itemstack) && state.getBlock().canSustainPlant(state, worldIn, pos, EnumFacing.UP, this) && worldIn.isAirBlock(pos.up()))
        {
            worldIn.setBlockState(pos.up(), getStateForPlacement(player, itemstack));

            if (player instanceof EntityPlayerMP)
            {
                CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)player, pos.up(), itemstack);
            }

            itemstack.shrink(1);
            return EnumActionResult.SUCCESS;
        }
        else
        {
            return EnumActionResult.FAIL;
        }
    }

    protected abstract IBlockState getStateFromEnum(E e);

    protected IBlockState getStateForPlacement(EntityPlayer player, ItemStack stack) {
        return getStateFromStack(stack);
    }

    protected IBlockState getStateFromStack(ItemStack stack) {
        return getStateFromEnum(getEnumFromStack(stack));
    }

    @Override
    public net.minecraft.block.state.IBlockState getPlant(net.minecraft.world.IBlockAccess world, BlockPos pos)
    {
        return world.getBlockState(pos);
    }

    @Override
    @Nonnull
    public String getTranslationKey(ItemStack stack) {
        return prefix + getEnumFromStack(stack).name().toLowerCase(Locale.ENGLISH);
    }

    @Nonnull
    protected ItemStack getCreativeStack(E object) {
        return getStackFromEnum(object, 1);
    }

    @Override
    public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> items) {
        if (isInCreativeTab(tab)) {
            for (E e : values) {
                ItemStack stack = getCreativeStack(e);
                if (!stack.isEmpty()) {
                    items.add(stack);
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModels() {
        for (E e: values) {
            ModelLoader.setCustomModelResourceLocation(this, e.ordinal(), new ModelResourceLocation(Objects.requireNonNull(getRegistryName()), e.name().toLowerCase(Locale.ENGLISH)));
        }
    }
}
