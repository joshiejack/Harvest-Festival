package joshie.harvest.core.base;

import joshie.harvest.api.core.ICreativeSorted;
import joshie.harvest.api.core.IShippable;
import joshie.harvest.api.core.ISizeable;
import joshie.harvest.api.core.ISizeable.Size;
import joshie.harvest.api.core.ISizedProvider;
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

public class ItemSizeable extends ItemHFEnum<ItemHFEnum, Size> implements IShippable, ICreativeSorted, ISizedProvider {
    private final Sizeable sizeable;
    private final String unlocalised;

    public ItemSizeable(String name, Sizeable sizeable) {
        super(Size.class);
        this.sizeable = sizeable;
        this.sizeable.setItem(this);
        this.unlocalised = name;
    }

    @Override
    public ISizeable getSizeable(ItemStack stack) {
        return sizeable;
    }

    @Override
    public Size getSize(ItemStack stack) {
        return Size.values()[Math.min(2, stack.getItemDamage())];
    }

    @Override
    public int getSortValue(ItemStack stack) {
        return CreativeSort.SIZEABLE + stack.getItemDamage() + (SizeableRegistry.REGISTRY.getId(sizeable) * 3);
    }

    @Override
    public long getSellValue(ItemStack stack) {
        return getSizeable(stack).getValue(getSize(stack));
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return unlocalised.toLowerCase() + "_" + getSize(stack).toString().toLowerCase();
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        Size sizeof = getSize(stack);
        String text = Text.translate("sizeable.format");
        String size = Text.translate("sizeable." + sizeof.name().toLowerCase());
        String name = Text.translate("sizeable." + unlocalised);
        text = StringUtils.replace(text, "%S", size);
        text = StringUtils.replace(text, "%P", name);
        return text;
    }

    @Override
    protected String getPrefix(Size size) {
        return unlocalised;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> list) {
        for (Size size: Size.values()) {
            list.add(new ItemStack(item, 1, size.ordinal()));
        }
    }
}