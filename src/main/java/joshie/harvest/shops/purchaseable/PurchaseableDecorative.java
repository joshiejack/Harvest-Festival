package joshie.harvest.shops.purchaseable;

import joshie.harvest.core.util.Text;
import net.minecraft.item.ItemStack;

import java.util.List;

import static net.minecraft.util.text.TextFormatting.YELLOW;

public class PurchaseableDecorative extends Purchaseable {
    public PurchaseableDecorative(int cost, ItemStack stack) {
        super(cost, stack);
    }

    @Override
    public void addTooltip(List<String> list) {
        for (ItemStack stack : stacks) {
            list.add(YELLOW + stack.getDisplayName());
            list.add(Text.translate("tooltip.cosmetic"));
        }
    }
}