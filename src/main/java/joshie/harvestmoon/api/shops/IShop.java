package joshie.harvestmoon.api.shops;

import java.util.List;

import joshie.harvestmoon.api.core.Weekday;
import net.minecraft.item.ItemStack;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public interface IShop {
   /** Whether the shop is currently open
    *  @param      the world the player is in
    *  @return     true if shop is open, false if its closed */
    public boolean isOpen(World world);

    /** Returns a list of every purchaseable item
     *  in this shop */
    public List<IPurchaseable> getContents();
    
    /** Add a new purchaseable to this shop **/
    public IShop addItem(IPurchaseable item);
    
    /** Convenience method for basic items
     *  @param      how much this costs in gold
     *  @param      the items you get for this purchase**/
    public IShop addItem(long cost, ItemStack... stack);
    
    /** Add some opening hours for this shop, based on difficulty, and day of the week
     *  No point in adding peaceful hours. Shops are always open 24 hours in peaceful.
     *  @param      the difficulty
     *  @param      the day of the week
     *  @param      the opening time (0-24000)
     *  @param      the closing time (0-24000) **/
    public IShop addOpening(EnumDifficulty difficulty, Weekday day, int opening, int closing);
    
    /** Hours, auto adjusts based on difficulty instead of manually adding **/
    public IShop addOpening(Weekday day, int opening, int closing);
    
    /** Return the welcome message for this shop **/
    public String getWelcome();

    /** Return the gui handler for this shop
     *  This is called whenever we are displaying the
     *  gui for the shop, in order to render the background;  */
    @SideOnly(Side.CLIENT)
    public IShopGuiOverlay getGuiOverlay();
    
    /** Make sure you only call this on the client **/
    @SideOnly(Side.CLIENT)
    public IShop setGuiOverlay(IShopGuiOverlay overlay);
}
