package joshie.harvestmoon.api.shops;

import java.util.List;

import joshie.harvestmoon.calendar.CalendarDate;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IPurchaseable {
    /** Whether this player can purchase this product or not, on this date/time **/
    public boolean canBuy(World world, EntityPlayer player);
    
    /** The cost of this product **/
    public long getCost();
    
    /** The product(s) that were purchased **/
    public ItemStack[] getProducts();

    /** Display tooltip for this item **/
    public void addTooltip(List list);
}
