package joshie.harvestmoon.shops;

import joshie.harvestmoon.calendar.CalendarDate;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IPurchaseable {
    /** Whether this player can purchase this product or not, on this date/time **/
    public boolean canBuy(World world, CalendarDate playersBirthday, CalendarDate dat);
    
    /** The cost of this product **/
    public long getCost();
    
    /** The product(s) that were purchased **/
    public ItemStack[] getProducts();
}
