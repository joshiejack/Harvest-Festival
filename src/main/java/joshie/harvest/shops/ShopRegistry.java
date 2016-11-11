package joshie.harvest.shops;

import joshie.harvest.api.npc.INPC;
import joshie.harvest.api.shops.IShopRegistry;
import joshie.harvest.core.util.annotations.HFApiImplementation;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;

@HFApiImplementation
public class ShopRegistry implements IShopRegistry {
    public static final ShopRegistry INSTANCE = new ShopRegistry();
    public final HashMap<ResourceLocation, Shop> shops = new HashMap<>();

    private ShopRegistry() {}

    @Override
    public Shop newShop(ResourceLocation resource, INPC npc) {
        Shop shop = new Shop(resource);
        npc.setShop(shop);
        shops.put(resource, shop);
        return shop;
    }

    @Override
    public Shop getShop(ResourceLocation resource) {
        return shops.get(resource);
    }
}
