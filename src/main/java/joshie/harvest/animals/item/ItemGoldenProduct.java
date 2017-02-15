package joshie.harvest.animals.item;

import joshie.harvest.animals.item.ItemAnimalProduct.Sizeable;
import joshie.harvest.api.HFApi;
import joshie.harvest.core.base.item.ItemHFEnum;
import joshie.harvest.core.helpers.TextHelper;
import joshie.harvest.core.lib.CreativeSort;
import net.minecraft.item.ItemStack;

public class ItemGoldenProduct extends ItemHFEnum<ItemGoldenProduct, Sizeable> {
    public ItemGoldenProduct() {
        super(Sizeable.class);
        for (Sizeable e: values) {
            long value = e.getGolden();
            if (value > 0L) {
                HFApi.shipping.registerSellable(getStackFromEnum(e), value);
            }
        }
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        String size = TextHelper.translate("sizeable.golden");
        String name = TextHelper.translate(prefix + "." + getEnumFromStack(stack).getName());
        String format = TextHelper.translate("sizeable.format");
        return String.format(format, size, name);
    }

    @Override
    public int getSortValue(ItemStack stack) {
        return CreativeSort.SIZEABLE + 4 + (getEnumFromStack(stack).ordinal() * 4);
    }
}