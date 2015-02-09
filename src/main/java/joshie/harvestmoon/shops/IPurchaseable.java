package joshie.harvestmoon.shops;

import joshie.harvestmoon.calendar.CalendarDate;
import joshie.harvestmoon.player.PlayerDataServer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IPurchaseable {
    /** Whether this player can purchase this product or not, on this date/time **/
    public boolean canBuy(World world, PlayerDataServer player, CalendarDate calendar);
    
    /** The cost of this product **/
    public int getCost();
    
    /** The product **/
    public ItemStack getProduct();
}
