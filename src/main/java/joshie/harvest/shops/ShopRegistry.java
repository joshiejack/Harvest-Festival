package joshie.harvest.shops;

import joshie.harvest.api.npc.INPC;
import joshie.harvest.api.shops.IShop;
import joshie.harvest.api.shops.IShopRegistry;
import joshie.harvest.core.util.HFApiImplementation;

import java.util.HashMap;

@HFApiImplementation
public class ShopRegistry implements IShopRegistry {
    public static final ShopRegistry INSTANCE = new ShopRegistry();
    private final HashMap<String, IShop> shops = new HashMap<>();

    private ShopRegistry() {}

    @Override
    public IShop newShop(String unlocalised, INPC npc) {
        IShop shop = new Shop(unlocalised);
        npc.setShop(shop);
        shops.put(unlocalised, shop);
        return shop;
    }

    @Override
    public IShop getShop(String unlocalised) {
        return shops.get(unlocalised);
    }
}
