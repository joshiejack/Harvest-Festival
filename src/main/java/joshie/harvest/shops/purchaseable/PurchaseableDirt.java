package joshie.harvest.shops.purchaseable;

import joshie.harvest.core.util.Translate;
import net.minecraft.item.ItemStack;

import java.util.List;

import static net.minecraft.util.text.TextFormatting.WHITE;

public class PurchaseableDirt extends Purchaseable {
    public PurchaseableDirt(int cost, ItemStack stack) {
        super(cost, stack);
    }

    @Override
    public void addTooltip(List<String> list) {
        for (ItemStack stack : stacks) {
            list.add(WHITE + stack.getDisplayName());
            list.add(Translate.translate("tooltip.dirt"));
        }
    }
}