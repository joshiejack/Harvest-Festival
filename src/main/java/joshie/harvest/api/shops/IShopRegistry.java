package joshie.harvest.api.shops;

import joshie.harvest.api.npc.INPC;

public interface IShopRegistry {
    /** Creates a new shop, and assigns it to this npc
     *  If this npc, already has a shop associated with it.
     *  Then this will return null;
     * @param       unlocalised name of the shop
     * @param       texture the shop uses
     * @param       the npc
     * @return      the shop*/
    public IShop newShop(String unlocalised, INPC npc);
    
    /** Grabs an existing shop based on it's unlocalised name **/
    public IShop getShop(String unlocalised);
}
