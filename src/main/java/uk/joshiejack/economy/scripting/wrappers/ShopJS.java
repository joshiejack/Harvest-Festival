package uk.joshiejack.economy.scripting.wrappers;

import uk.joshiejack.economy.shop.Shop;
import uk.joshiejack.penguinlib.scripting.wrappers.AbstractJS;

public class ShopJS extends AbstractJS<Shop> {
    public ShopJS(Shop shop) {
        super(shop);
    }

    public String id() {
        return penguinScriptingObject.id();
    }
}
