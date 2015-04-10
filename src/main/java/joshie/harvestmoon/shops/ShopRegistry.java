package joshie.harvestmoon.shops;

import java.util.HashMap;

import joshie.harvestmoon.api.npc.INPC;
import joshie.harvestmoon.api.shops.IShop;
import joshie.harvestmoon.api.shops.IShopRegistry;

public class ShopRegistry implements IShopRegistry {
    private HashMap<String, IShop> shops = new HashMap();
    
    @Override
    public IShop newShop(String unlocalised, INPC npc) {
        IShop shop = new ShopInventory(unlocalised);
        npc.setShop(shop);
        shops.put(unlocalised, shop);
        return shop;
    }

    @Override
    public IShop getShop(String unlocalised) {
        return shops.get(unlocalised);
    }
}
