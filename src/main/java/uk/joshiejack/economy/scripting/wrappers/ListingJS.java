package uk.joshiejack.economy.scripting.wrappers;

import uk.joshiejack.economy.shop.Listing;
import uk.joshiejack.economy.shop.Sublisting;
import uk.joshiejack.penguinlib.scripting.WrapperRegistry;
import uk.joshiejack.penguinlib.scripting.wrappers.AbstractJS;
import uk.joshiejack.penguinlib.scripting.wrappers.ItemStackJS;

public class ListingJS extends AbstractJS<Listing> {
    public ListingJS(Listing listing) {
        super(listing);
    }

    @SuppressWarnings("unchecked")
    public ItemStackJS asItem(SublistingJS sublisting) {
        Sublisting sub = sublisting.penguinScriptingObject;
        return WrapperRegistry.wrap(sub.getHandler().createIcon(sub.getObject())[0]);
    }

    public SublistingJS sublisting(int id) {
        Sublisting sublisting = penguinScriptingObject.getSublistingByID(id);
        return sublisting == null ? null : WrapperRegistry.wrap(sublisting);
    }

    public String department() {
        return penguinScriptingObject.getDepartment().id();
    }

    public String id() {
        return penguinScriptingObject.getID();
    }
}
