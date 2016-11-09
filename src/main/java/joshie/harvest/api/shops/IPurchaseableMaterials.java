package joshie.harvest.api.shops;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public interface IPurchaseableMaterials extends IPurchasable {
    /** The purchaseables id, this needs to be a unique string **/
    String getPurchaseableID();

    /** This is whether the item can be listed in the shop
     *  @param world the world object
     *  @param player the player trying to buy**/
    default boolean canList(World world, EntityPlayer player) { return getCost() < 0 || canBuy(world, player, 1); }

    /** Returns a list of requirements to be displayed for this purchasable **/
    IRequirement[] getRequirements();

}