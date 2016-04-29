package joshie.harvest.shops;

import joshie.harvest.api.npc.INPC;
import joshie.harvest.api.shops.IShop;
import joshie.harvest.api.shops.IShopRegistry;

import java.util.HashMap;

public class ShopRegistry implements IShopRegistry {
    private HashMap<String, IShop> shops = new HashMap<String, IShop>();
    
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
