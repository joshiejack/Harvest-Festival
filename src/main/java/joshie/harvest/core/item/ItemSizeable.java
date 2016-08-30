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
import org.apache.commons.lang3.StringUtils;

import java.util.List;

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
        return SizeableRegistry.REGISTRY.getValues().get(real);
    }

    @Override
    public Size getSize(ItemStack stack) {
        return Size.values()[Math.min(2, stack.getItemDamage())];
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
        return getObjectFromStack(stack).getUnlocalizedName().toLowerCase() + "_" + getSize(stack).toString().toLowerCase();
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        Size sizeof = getSize(stack);
        String text = Text.translate("sizeable.format");
        String size = Text.translate("sizeable." + sizeof.name().toLowerCase());
        String name = Text.translate("sizeable." + getObjectFromStack(stack).getUnlocalizedName());
        text = StringUtils.replace(text, "%S", size);
        text = StringUtils.replace(text, "%P", name);
        return text;
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