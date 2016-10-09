package joshie.harvest.shops;

import joshie.harvest.api.npc.INPC;
import joshie.harvest.api.shops.IShop;
import joshie.harvest.api.shops.IShopRegistry;
import joshie.harvest.core.util.annotations.HFApiImplementation;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;

@HFApiImplementation
public class ShopRegistry implements IShopRegistry {
    public static final ShopRegistry INSTANCE = new ShopRegistry();
    public final HashMap<ResourceLocation, IShop> shops = new HashMap<>();

    private ShopRegistry() {}

    @Override
    public IShop newShop(ResourceLocation resource, INPC npc) {
        IShop shop = new Shop(resource);
        npc.setShop(shop);
        shops.put(resource, shop);
        return shop;
    }

    @Override
    public IShop getShop(ResourceLocation resource) {
        return shops.get(resource);
    }
}
