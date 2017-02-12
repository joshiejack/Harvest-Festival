package joshie.harvest.api.shops;

import joshie.harvest.api.core.ISpecialRules;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.List;

public interface IPurchasable extends ISpecialRules {
    /** The purchaseables id, this needs to be a unique string **/
    String getPurchaseableID();

    /** This is whether the item can be listed in the shop
     *  It is called when attempting to display an item
     *  @param world the world object
     *  @param player the player trying to buy**/
    default boolean canList(@Nonnull World world, @Nonnull EntityPlayer player) { return getCost() < 0 || canDo(world, player, 1); }

    /** The total cost of this item **/
    long getCost();

    /** How many of this item be be purchased each day **/
    default int getStock() { return getCost() < 0 ? 10 : Integer.MAX_VALUE; }

    /** This is the display name for this item **/
    default String getDisplayName() { return getDisplayStack().getDisplayName(); }

    /** This is what this will be displayed as in the store **/
    ItemStack getDisplayStack();

    /** Add tooltip information for this item **/
    @SideOnly(Side.CLIENT)
    void addTooltip(List<String> list);

    /** Called whenever this item is purchased by this player
     * @param player    the player doing the purchasing **/
    void onPurchased(EntityPlayer player);
}