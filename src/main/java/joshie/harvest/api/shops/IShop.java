package joshie.harvest.api.shops;

import joshie.harvest.api.calendar.Weekday;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public interface IShop {
    /** Whether the shop is currently open
     *  @param      world the world the player is in
     *  @param      player the player (can be null if checking if the npcs ai should return)
     *  @return     true if shop is open, false if its closed */
    boolean isOpen(World world, @Nullable EntityPlayer player);

    /** If this shop is going to open soon
     * @return whether it will open soon or not */
    boolean isPreparingToOpen(World world);

    /** Returns a list of every purchaseable item
     *  in this shop */
    List<IPurchaseable> getContents(EntityPlayer player);

    /** Add a new purchaseable to this shop **/
    IShop addItem(IPurchaseable item);

    /** Convenience method for basic items
     *  @param      cost how much this costs in gold
     *  @param      stack the items you get for this purchase**/
    IShop addItem(long cost, ItemStack... stack);

    /** Add some opening hours for this shop, based on difficulty, and day of the week
     *  No point in adding peaceful hours. Shops are always open 24 hours in peaceful.
     *  @param      difficulty the difficulty
     *  @param      day the day of the week
     *  @param      opening the opening time (0-24000)
     *  @param      closing the closing time (0-24000) **/
    IShop addOpening(EnumDifficulty difficulty, Weekday day, int opening, int closing);

    /** Hours, auto adjusts based on difficulty instead of manually adding **/
    IShop addOpening(Weekday day, int opening, int closing);

    /** Return the welcome message for this shop **/
    String getWelcome();

    /** Return the gui handler for this shop
     *  This is called whenever we are displaying the
     *  gui for the shop, in order to render the background;  */
    @SideOnly(Side.CLIENT)
    IShopGuiOverlay getGuiOverlay();

    /** Make sure you only call this on the client **/
    @SideOnly(Side.CLIENT)
    IShop setGuiOverlay(IShopGuiOverlay overlay);
}