package joshie.harvest.api.shops;

import joshie.harvest.api.calendar.Weekday;
import joshie.harvest.api.core.ISpecialPurchaseRules;
import net.minecraft.item.ItemStack;

public interface IShop {
    /** Hours, auto adjusts based on difficulty instead of manually adding
     *  @param day the day of the week
     *  @param opening the opening time (0-24000)
     *  @param closing the closing time (0-24000)**/
    IShop addOpening(Weekday day, int opening, int closing);

    /** Adds a new purchaseable item to the shop
     * @param item  the purchaseable **/
    IShop addItem(IPurchasable item);

    /** Convenience method for basic items
     *  @param      cost how much this costs in gold
     *  @param      stack the items you get for this purchase**/
    IShop addItem(long cost, ItemStack... stack);

    /** Set special rules for being able to buy or sell from this shop
     *  By default all shops are accessible at all times
     *  @param rules the rules you want to set **/
    IShop setSpecialPurchaseRules(ISpecialPurchaseRules rules);
}