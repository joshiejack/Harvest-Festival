package joshie.harvest.api.shops;

import joshie.harvest.api.calendar.Weekday;
import net.minecraft.item.ItemStack;
import net.minecraft.world.EnumDifficulty;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IShop {
    /** Add some opening hours for this shop, based on difficulty, and day of the week
     *  No point in adding peaceful hours. Shops are always open 24 hours in peaceful.
     *  @param      difficulty the difficulty
     *  @param      day the day of the week
     *  @param      opening the opening time (0-24000)
     *  @param      closing the closing time (0-24000) **/
    IShop addOpening(EnumDifficulty difficulty, Weekday day, int opening, int closing);

    /** Hours, auto adjusts based on difficulty instead of manually adding
     *  @param day the day of the week
     *  @param opening the opening time (0-24000)
     *  @param closing the closing time (0-24000)**/
    IShop addOpening(Weekday day, int opening, int closing);

    /** Adds a new purchaseable item to the shop
     * @param item  the purchaseable **/
    IShop addItem(IPurchaseable item);

    /** Convenience method for basic items
     *  @param      cost how much this costs in gold
     *  @param      stack the items you get for this purchase**/
    IShop addItem(long cost, ItemStack... stack);

    /** Make sure you only call this on the client,
     *  Allows you to render something over the background of your shop **/
    @SideOnly(Side.CLIENT)
    IShop setGuiOverlay(IShopGuiOverlay overlay);
}