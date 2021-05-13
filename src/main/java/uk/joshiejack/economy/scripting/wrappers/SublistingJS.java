package uk.joshiejack.economy.scripting.wrappers;

import uk.joshiejack.economy.shop.Sublisting;
import uk.joshiejack.penguinlib.scripting.WrapperRegistry;
import uk.joshiejack.penguinlib.scripting.wrappers.AbstractJS;
import uk.joshiejack.penguinlib.scripting.wrappers.ItemStackJS;

public class SublistingJS extends AbstractJS<Sublisting> {
    public SublistingJS(Sublisting purchasable) {
        super(purchasable);
    }

    @SuppressWarnings("unchecked")
    public ItemStackJS item() {
        Sublisting object = penguinScriptingObject;
        return WrapperRegistry.wrap(object.getHandler().createIcon(object.getObject())[0]);
    }

    public long gold() {
        return penguinScriptingObject.getGold();
    }

    public String id() {
        return penguinScriptingObject.id();
    }
}
