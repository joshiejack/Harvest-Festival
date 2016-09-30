package joshie.harvest.core.item;

import joshie.harvest.api.core.IShippable;
import joshie.harvest.api.core.ISizeable.Size;
import joshie.harvest.api.core.ISizedProvider;
import joshie.harvest.core.base.item.ItemHFFML;
import joshie.harvest.core.handlers.SizeableRegistry;
import joshie.harvest.core.lib.CreativeSort;
import joshie.harvest.core.lib.Sizeable;
import joshie.harvest.core.util.Text;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Locale;

public class ItemSizeable extends ItemHFFML<ItemSizeable, Sizeable> implements IShippable, ISizedProvider {
    public ItemSizeable() {
        super(SizeableRegistry.REGISTRY);
    }

    @Override
    public Sizeable getNullValue() {
        return null;
    }

    @Override
    public Sizeable getObjectFromStack(ItemStack stack) {
        int real = (int)Math.floor(stack.getItemDamage() / 3);
        int id = Math.max(0, Math.min(SizeableRegistry.REGISTRY.getValues().size() - 1, real));
        return SizeableRegistry.REGISTRY.getValues().get(id);
    }

    @Override
    public Size getSize(ItemStack stack) {
        return Size.values()[Math.min(2, stack.getItemDamage() % 3)];
    }

    @Override
    public int getSortValue(ItemStack stack) {
        return CreativeSort.SIZEABLE + stack.getItemDamage() + (SizeableRegistry.REGISTRY.getValues().indexOf(getObjectFromStack(stack)) * 3);
    }

    @Override
    public long getSellValue(ItemStack stack) {
        return getObjectFromStack(stack).getValue(getSize(stack));
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return getObjectFromStack(stack).getUnlocalizedName().toLowerCase(Locale.ENGLISH) + "_" + getSize(stack).toString().toLowerCase(Locale.ENGLISH);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        Size sizeof = getSize(stack);
        String size = Text.translate("sizeable." + sizeof.name().toLowerCase(Locale.ENGLISH));
        String name = Text.translate("sizeable." + getObjectFromStack(stack).getUnlocalizedName());
        String format = Text.translate("sizeable.format");
        return String.format(format, size, name);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> list) {
        for (Sizeable sizeable: SizeableRegistry.REGISTRY) {
            for (Size size: Size.values()) {
                list.add(sizeable.getStack(size));
            }
        }
    }
}