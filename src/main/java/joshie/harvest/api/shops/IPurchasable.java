package joshie.harvest.api.shops;

import joshie.harvest.api.core.ISpecialPurchaseRules;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public interface IPurchasable extends ISpecialPurchaseRules {
    /** The purchaseables id, this needs to be a unique string **/
    String getPurchaseableID();

    /** This is whether the item can be listed in the shop
     *  @param world the world object
     *  @param player the player trying to buy**/
    default boolean canList(World world, EntityPlayer player) { return getCost() < 0 || canBuy(world, player, 1); }

    /** The total cost of this item **/
    long getCost();

    /** This is what this will be displayed as in the store **/
    ItemStack getDisplayStack();

    /** Add tooltip information for this item **/
    @SideOnly(Side.CLIENT)
    void addTooltip(List<String> list);

    /** Called whenever this item is purchased by this player
     * @param player    the player doing the purchasing **/
    void onPurchased(EntityPlayer player);




}