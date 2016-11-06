package joshie.harvest.api.shops;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public interface IPurchasableBuilder extends IPurchasable {
    /** The purchaseables id, this needs to be a unique string **/
    String getPurchaseableID();

    /** This is whether the item can be listed in the shop
     *  @param world the world object
     *  @param player the player trying to buy**/
    default boolean canList(World world, EntityPlayer player) { return getCost() < 0 || canBuy(world, player, 1); }

    /** The amount of wood this costs **/
    int getLogCost();

    /** The amount of stine this costs **/
    int getStoneCost();
}