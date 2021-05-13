package uk.joshiejack.penguinlib.item.base;

import uk.joshiejack.penguinlib.PenguinConfig;
import uk.joshiejack.penguinlib.item.ItemDinnerware;
import uk.joshiejack.penguinlib.item.PenguinItems;
import uk.joshiejack.penguinlib.item.interfaces.Edible;
import uk.joshiejack.penguinlib.item.interfaces.IPenguinItem;
import uk.joshiejack.penguinlib.util.interfaces.IPenguinMulti;
import uk.joshiejack.penguinlib.util.helpers.forge.RegistryHelper;
import uk.joshiejack.penguinlib.util.helpers.generic.ArrayHelper;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.*;

public class ItemMultiEdible<E extends Enum<E> & Edible> extends ItemFood implements IPenguinItem, IPenguinMulti {
    private final Map<String, E> names = new HashMap<>();
    private final Class<E> enumClass;
    private final EnumMap<E, ItemDinnerware.Dinnerware> dinnerware;
    private final E[] values;
    private final String prefix;

    public ItemMultiEdible(ResourceLocation registry, Class<E> clazz) {
        super(0, 0.0F, false);
        enumClass = clazz;
        values = clazz.getEnumConstants();
        prefix = registry.getNamespace() + ".item." + registry.getPath() + ".";
        dinnerware =  new EnumMap<>(enumClass);
        PenguinConfig.forceDinnerwareItem = true;
        setHasSubtypes(true); //metadata
        RegistryHelper.setRegistryAndLocalizedName(registry, this);
    }

    public void registerContainer(E meal, ItemDinnerware.Dinnerware container) {
        dinnerware.put(meal, container);
    }

    @Nonnull
    protected ItemStack getLeftovers(ItemStack stack) {
        E meal = getEnumFromStack(stack);
        ItemDinnerware.Dinnerware container = dinnerware.get(meal);
        if (container != null) {
            return PenguinItems.DINNERWARE.getStackFromEnum(container);
        } else return ItemStack.EMPTY;
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

    public ItemStack getStackFromEnum(E e) {
        return getStackFromEnum(e, 1);
    }

    @Override
    public ItemStack getStackFromEnumString(String name, int size) {
        return getStackFromEnum(Enum.valueOf(enumClass, name.toUpperCase()), size);
    }

    public ItemStack getStackFromString(String name) {
        E e = names.get(name);
        if (e == null) {
            for (E value: values) {
                if (value.name().toLowerCase(Locale.ENGLISH).equals(name)) {
                    e = value;
                    names.put(name, e);
                }
            }
        }

        return getStackFromEnum(e);
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }

    @Override
    public int getHealAmount(ItemStack stack) {
        return getEnumFromStack(stack).getHunger();
    }

    @Override
    public float getSaturationModifier(ItemStack stack) {
        return getEnumFromStack(stack).getSaturation();
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return getEnumFromStack(stack).getConsumeTime();
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return getEnumFromStack(stack).getAction();
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
    protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player) {
        ItemStack leftovers = getLeftovers(stack);
        if (leftovers.isEmpty() && stack.getCount() > 0) {
            player.addItemStackToInventory(leftovers);
        }
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

    public E[] getValues() {
        return values;
    }
}
