package uk.joshiejack.penguinlib.item.base.block;

import uk.joshiejack.penguinlib.block.interfaces.IPenguinBlock;
import uk.joshiejack.penguinlib.item.interfaces.IPenguinItem;
import uk.joshiejack.penguinlib.util.interfaces.IPenguinMulti;
import uk.joshiejack.penguinlib.util.helpers.forge.RegistryHelper;
import uk.joshiejack.penguinlib.util.helpers.generic.ArrayHelper;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemBlockMultiLeaves<E extends Enum<E> & IStringSerializable> extends ItemBlock implements IPenguinItem, IPenguinMulti<E> {
    private final Class<E> enumClass;
    private final E[] values;
    private final String prefix;
    private final IPenguinBlock penguin;

    public ItemBlockMultiLeaves(ResourceLocation registry, Class<E> clazz, IPenguinBlock block) {
        super((Block) block);
        enumClass = clazz;
        values = clazz.getEnumConstants();
        prefix = registry.getNamespace() + ".block." + registry.getPath() + ".";
        penguin = block;
        setMaxDamage(0);
        setHasSubtypes(true); //metadata
        RegistryHelper.setRegistryAndLocalizedName(registry, this);
    }

    @Override
    public E[] getValues() {
        return values;
    }

    private E getEnumFromStack(ItemStack stack) {
        return getEnumFromMeta(stack.getItemDamage());
    }

    private E getEnumFromMeta(int meta) {
        return ArrayHelper.getArrayValue(values, meta);
    }

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

    @Override
    @Nonnull
    public String getTranslationKey(ItemStack stack) {
        return prefix + getEnumFromStack(stack).getName();
    }

    @Nullable
    protected ItemStack getCreativeStack(E object) {
        return getStackFromEnum(object, 1);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> items) {
        if (isInCreativeTab(tab)) {
            for (E e : values) {
                ItemStack stack = getCreativeStack(e);
                if (stack != null) {
                    items.add(stack);
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModels() {
        penguin.registerModels(this);
    }
}
