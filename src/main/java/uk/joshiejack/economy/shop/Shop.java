package uk.joshiejack.economy.shop;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import uk.joshiejack.penguinlib.util.helpers.generic.StringHelper;
import net.minecraft.util.ResourceLocation;

import java.util.List;
import java.util.Map;

public class Shop {
    private static final Map<Department, Shop> subshops = Maps.newHashMap();
    private final List<Department> departments = Lists.newArrayList();
    private String id;
    private final String name;
    private Department last;

    public Shop(String shop_id, String name) {
        this.id = shop_id;
        if (name.contains(":")) {
            String[] split = ResourceLocation.splitObjectName(name);
            this.name = split[0] + ".shop." + split[1] + ".name";
        } else this.name = name;
    }

    public String id() {
        return id;
    }

    public String getLocalizedName() {
        return StringHelper.localize(name);
    }

    public List<Department> getDepartments() {
        return departments;
    }

    public static Shop get(Department shop) {
        Shop market = shop.getShop() != null ? shop.getShop() : subshops.get(shop);
        if (market != null && market.last == null) {
            market.last = shop;
        }

        return market;
    }

    public Department getLast() {
        return last;
    }

    public void setLast(Department last) {
        this.last = last;
    }
}
