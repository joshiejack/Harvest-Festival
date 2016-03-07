package joshie.harvest.shops.purchaseable;

import java.util.List;

import joshie.harvest.core.util.Translate;
import joshie.harvest.core.util.generic.Text;
import net.minecraft.item.ItemStack;

public class PurchaseableDirt extends Purchaseable {    
    public PurchaseableDirt(int cost, ItemStack stack) {
        super(cost, stack);
    }

    @Override
    public void addTooltip(List list) {
        for (ItemStack stack: stacks) {
        list.add(Text.WHITE + stack.getDisplayName());
		list.add(Translate.translate("tooltip.dirt"));
    }
}
}
