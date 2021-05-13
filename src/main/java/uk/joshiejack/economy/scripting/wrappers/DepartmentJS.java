package uk.joshiejack.economy.scripting.wrappers;

import uk.joshiejack.economy.shop.Department;
import uk.joshiejack.economy.shop.Listing;
import uk.joshiejack.penguinlib.scripting.WrapperRegistry;
import uk.joshiejack.penguinlib.scripting.wrappers.AbstractJS;

import java.util.Objects;

public class DepartmentJS extends AbstractJS<Department> {
    public DepartmentJS(Department department) {
        super(department);
    }

    public ShopJS shop() {
        return WrapperRegistry.wrap(Objects.requireNonNull(penguinScriptingObject.getShop()));
    }

    public ListingJS listing(String id) {
        Listing listing = penguinScriptingObject.getListingByID(id);
        return listing == null ? null : WrapperRegistry.wrap(listing);
    }

    public String id() {
        return penguinScriptingObject.id();
    }
}
