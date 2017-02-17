package joshie.harvest.animals.item;

import joshie.harvest.animals.item.ItemAnimalProduct.Sizeable;
import joshie.harvest.api.HFApi;
import joshie.harvest.core.base.item.ItemHFFoodEnum;
import joshie.harvest.core.helpers.TextHelper;
import joshie.harvest.core.lib.CreativeSort;
import net.minecraft.item.ItemStack;

public class ItemGoldenProduct extends ItemHFFoodEnum<ItemGoldenProduct, Sizeable> {
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
    public int getHealAmount(ItemStack stack) {
        switch (getEnumFromStack(stack)) {
            case EGG:
            case MAYONNAISE:
                return 6;
            case MILK:
                return 5;
            default:
                return 0;
        }
    }

    @Override
    public float getSaturationModifier(ItemStack stack) {
        switch (getEnumFromStack(stack)) {
            case EGG:
                return 0.8F;
            case MAYONNAISE:
                return 1.0F;
            case MILK:
                return 0.6F;
            default:
                return 0;
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