package joshie.harvest.shops.purchasable;

import joshie.harvest.core.helpers.TextHelper;
import net.minecraft.item.ItemStack;

import java.util.List;

import static net.minecraft.util.text.TextFormatting.YELLOW;

public class PurchasableDecorative extends Purchasable {
    public PurchasableDecorative(int cost, ItemStack stack) {
        super(cost, stack);
    }

    @Override
    public void addTooltip(List<String> list) {
        for (ItemStack stack : stacks) {
            list.add(YELLOW + stack.getDisplayName());
            list.add(TextHelper.translate("tooltip.cosmetic"));
        }
    }
}