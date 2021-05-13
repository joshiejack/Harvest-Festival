package uk.joshiejack.penguinlib.item.base;

import uk.joshiejack.penguinlib.item.interfaces.IPenguinItem;
import uk.joshiejack.penguinlib.util.interfaces.IPenguinMulti;
import uk.joshiejack.penguinlib.util.helpers.forge.RegistryHelper;
import uk.joshiejack.penguinlib.util.helpers.generic.ArrayHelper;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Locale;
import java.util.Objects;

public class ItemMulti<E extends Enum<E>> extends Item implements IPenguinItem, IPenguinMulti {
    private final Class<E> enumClass;
    private final E[] values;
    private final String prefix;

    public ItemMulti(ResourceLocation registry, Class<E> clazz) {
        enumClass = clazz;
        values = clazz.getEnumConstants();
        prefix = registry.getNamespace() + ".item." + registry.getPath() + ".";
        setHasSubtypes(true); //metadata
        RegistryHelper.setRegistryAndLocalizedName(registry, this);
    }

    @Override
    public Enum[] getValues() {
        return values;
    }

    public E getEnumFromStack(ItemStack stack) {
        return getEnumFromMeta(stack.getItemDamage());
    }

    private E getEnumFromMeta(int meta) {
        return ArrayHelper.getArrayValue(values, meta);
    }

    @Nonnull
    public ItemStack getStackFromEnum(E e, int size) {
        return new ItemStack(this, size, e.ordinal());
    }

    public ItemStack getStackFromEnum(E e) {
        return getStackFromEnum(e, 1);
    }

    @Override
    public ItemStack getStackFromEnumString(String name, int size) {
        return getStackFromEnum(Enum.valueOf(enumClass, name.toUpperCase()), size);
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }

    @Nonnull
    @Override
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
        for (E e : values) {
            ModelLoader.setCustomModelResourceLocation(this, e.ordinal(), new ModelResourceLocation(Objects.requireNonNull(getRegistryName()), e.name().toLowerCase(Locale.ENGLISH)));
        }
    }
}
