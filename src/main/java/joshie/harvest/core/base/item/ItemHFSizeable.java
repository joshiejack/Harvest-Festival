package joshie.harvest.core.base.item;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.core.ISizedProvider;
import joshie.harvest.api.core.Size;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.helpers.TextHelper;
import joshie.harvest.core.lib.CreativeSort;
import joshie.harvest.core.util.interfaces.ISizeable;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Locale;

public class ItemHFSizeable<I extends ItemHFFoodEnum, E extends Enum<E> & IStringSerializable & ISizeable> extends ItemHFFoodEnum<I, E> implements ISizedProvider<E> {
    public ItemHFSizeable(Class<E> clazz) {
        this(HFTab.FARMING, clazz);
    }

    public ItemHFSizeable(CreativeTabs tab, Class<E> clazz) {
        super(tab, clazz);
        for (E e: values) {
            for (Size size: Size.values()) {
                long value = e.getSellValue(size);
                if (value > 0L) {
                    HFApi.shipping.registerSellable(getStack(e, size), value);
                }
            }
        }
    }

    @Override
    public E getEnumFromStack(ItemStack stack) {
        int real = (int)Math.floor(stack.getItemDamage() / 3);
        int id = Math.max(0, Math.min(values.length - 1, real));
        return values[id];
    }

    @Override
    public ItemStack getStackFromEnum(E e) {
        return e.getStack(this, Size.SMALL);
    }

    @Override
    public ItemStack getStackFromEnum(E e, int amount) {
        return e.getStackOfSize(this, Size.SMALL, amount);
    }

    public ItemStack getStack(E e, Size size) {
        return e.getStack(this, size);
    }

    @Override
    public ItemStack getStackOfSize(E e, Size size, int amount) {
        return e.getStackOfSize(this, size, amount);
    }

    @Override
    public Size getSize(ItemStack stack) {
        return Size.values()[Math.min(2, stack.getItemDamage() % 3)];
    }

    @Override
    public E getObject(ItemStack stack) {
        return getEnumFromStack(stack);
    }

    @Override
    public E getObjectFromString(String string) {
        for (E e: values) {
            if (e.getName().equalsIgnoreCase(string)) return e;
        }

        return values[0];
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        String size = TextHelper.translate("sizeable." + getSize(stack).name().toLowerCase(Locale.ENGLISH));
        String name = TextHelper.translate(prefix + "." + getEnumFromStack(stack).getName());
        String format = TextHelper.translate("sizeable.format");
        return String.format(format, size, name);
    }

    @Override
    public int getSortValue(ItemStack stack) {
        return CreativeSort.SIZEABLE + stack.getItemDamage() + (getEnumFromStack(stack).ordinal() * 3);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> list) {
        for (E e: values) {
            for (Size size: Size.values()) {
                list.add(e.getStack(item, size));
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModels(Item item, String name) {
        for (E e: values) {
            for (Size size: Size.values()) {
                ItemStack stack = e.getStack(item, size);
                ModelLoader.setCustomModelResourceLocation(stack.getItem(), stack.getItemDamage(), new ModelResourceLocation(getRegistryName(), e.getName() + "_" + size.getName()));
            }
        }
    }
}
