package joshie.harvest.api.shops;

import joshie.harvest.api.calendar.Weekday;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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

    /** Make sure you only call this on the client,
     *  Allows you to render something over the background of your shop **/
    @SideOnly(Side.CLIENT)
    IShop setGuiOverlay(IShopGuiOverlay overlay);
}