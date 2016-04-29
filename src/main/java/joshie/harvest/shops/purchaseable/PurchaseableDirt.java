package joshie.harvest.shops.purchaseable;

import joshie.harvest.core.util.Translate;
import joshie.harvest.core.util.generic.Text;
import net.minecraft.item.ItemStack;

import java.util.List;

public class PurchaseableDirt extends Purchaseable {
    public PurchaseableDirt(int cost, ItemStack stack) {
        super(cost, stack);
    }

    @Override
    public void addTooltip(List<String> list) {
        for (ItemStack stack : stacks) {
            list.add(Text.WHITE + stack.getDisplayName());
            list.add(Translate.translate("tooltip.dirt"));
        }
    }
}