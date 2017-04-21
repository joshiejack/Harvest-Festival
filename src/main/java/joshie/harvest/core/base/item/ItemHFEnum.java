package joshie.harvest.core.base.item;

import joshie.harvest.api.HFApi;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.helpers.TextHelper;
import joshie.harvest.core.lib.CreativeSort;
import joshie.harvest.core.util.interfaces.ICreativeSorted;
import joshie.harvest.core.util.interfaces.ISellable;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Locale;

public abstract class ItemHFEnum<I extends ItemHFEnum, E extends Enum<E> & IStringSerializable> extends ItemHFBase<I> implements ICreativeSorted {
    protected final Class<E> enumClass;
    protected final E[] values;
    protected final String prefix;

    public ItemHFEnum(Class<E> clazz) {
        this(HFTab.FARMING, clazz);
    }

    public ItemHFEnum(CreativeTabs tab, Class<E> clazz) {
        super(tab);
        enumClass = clazz;
        values = clazz.getEnumConstants();
        prefix = clazz.getSimpleName().toLowerCase(Locale.ENGLISH);
        setHasSubtypes(true);
        if (values[0] instanceof ISellable) {
            for (E e: values) {
                long value = ((ISellable)e).getSellValue();
                if (value > 0L) {
                    HFApi.shipping.registerSellable(getStackFromEnum(e), value);
                }
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public I setUnlocalizedName(String name) {
        super.setUnlocalizedName(name);
        return (I) this;
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }

    public E getEnumFromStack(ItemStack stack) {
        if (stack.getItem() != this) return null;

        return getEnumFromMeta(stack.getItemDamage());
    }

    public E getEnumFromMeta(int meta) {
        if (meta < 0 || meta >= values.length) {
            meta = 0;
        }

        return values[meta];
    }

    public ItemStack getStackFromEnum(E e) {
        return new ItemStack(this, 1, e.ordinal());
    }

    public ItemStack getStackFromEnum(E e, int size) {
        return new ItemStack(this, size, e.ordinal());
    }

    public ItemStack getStackFromEnumString(String name) {
        return getStackFromEnum(Enum.valueOf(enumClass, name.toUpperCase()));
    }

    @Override
    @Nonnull
    public String getUnlocalizedName(ItemStack stack) {
        return prefix + "_" + getEnumFromStack(stack).name().toLowerCase(Locale.ENGLISH);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return TextHelper.translate(getUnlocalizedName(stack).replaceAll("(.)([A-Z])", "$1$2").toLowerCase(Locale.ENGLISH).replace("_", "."));
    }

    @Override
    public int getSortValue(ItemStack stack) {
        return CreativeSort.NONE;
    }

    public boolean shouldDisplayInCreative(E e) {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(@Nonnull Item item, CreativeTabs tab, NonNullList<ItemStack> list) {
        for (E e: values) {
            if (shouldDisplayInCreative(e)) {
                list.add(new ItemStack(item, 1, e.ordinal()));
            }
        }
    }

    protected String getPrefix(E e) {
        return e.getClass().getSimpleName().toLowerCase(Locale.ENGLISH);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModels(Item item, String name) {
        for (E e: values) {
            ModelLoader.setCustomModelResourceLocation(item, e.ordinal(), new ModelResourceLocation(getRegistryName(), e.getName()));
        }
    }
}