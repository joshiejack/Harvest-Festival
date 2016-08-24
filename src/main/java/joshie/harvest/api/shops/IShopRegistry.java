package joshie.harvest.api.shops;

import joshie.harvest.api.npc.INPC;
import net.minecraft.util.ResourceLocation;

public interface IShopRegistry {
    /** Creates a new shop, and assigns it to this npc
     *  If this npc, already has a shop associated with it.
     *  Then this will return null;
     * @param       resource name of the shop
     * @param       npc the npc
     * @return      the shop*/
    IShop newShop(ResourceLocation resource, INPC npc);
    
    /** Grabs an existing shop based on it's resource name
     * @param resource  the shop you want **/
    IShop getShop(ResourceLocation resource);
}