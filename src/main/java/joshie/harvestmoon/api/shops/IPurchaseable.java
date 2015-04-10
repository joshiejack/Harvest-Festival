package joshie.harvestmoon.api.shops;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IPurchaseable {
    /** Whether this player can purchase this product or not, on this date/time **/
    public boolean canBuy(World world, EntityPlayer player);
    
    /** The cost of this product **/
    public long getCost();
    
    /** This is the itemstack that gets displayed in the shop view **/
    public ItemStack getDisplayStack();
    
    /** Called whenever this item is purchased
     *  @param      the player purchasing the item
     *  @return     return true if the gui should close after a purchase **/
    public boolean onPurchased(EntityPlayer player);

    /** Display tooltip for this item **/
    public void addTooltip(List list);
}
