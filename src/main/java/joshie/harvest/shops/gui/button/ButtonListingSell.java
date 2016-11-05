package joshie.harvest.shops.gui.button;

import joshie.harvest.api.shops.IPurchasable;
import joshie.harvest.core.helpers.MCClientHelper;
import joshie.harvest.shops.gui.GuiNPCShop;

public class ButtonListingSell extends ButtonListing {
    public ButtonListingSell(GuiNPCShop shop, IPurchasable purchasable, int buttonId, int x, int y) {
        super(shop, purchasable, buttonId, x, y);
    }

    @Override
    protected boolean canPurchase10() {
        return purchasable.canBuy(MCClientHelper.getWorld(), MCClientHelper.getPlayer(), 10);
    }

    @Override
    protected boolean canPurchase1() {
        return purchasable.canBuy(MCClientHelper.getWorld(), MCClientHelper.getPlayer(), 1);
    }
}
