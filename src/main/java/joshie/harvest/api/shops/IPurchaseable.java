package joshie.harvest.api.shops;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IPurchaseable {
    /** Whether the player has everything needed to purchase this item **/
    public boolean canBuy(World world, EntityPlayer player);
    
    /** Whether this item shows up in the shop gui for purchase **/
    public boolean canList(World world, EntityPlayer player);
    
    /** The cost of this product **/
    public long getCost();
    
    /** This is the itemstack that gets displayed in the shop view **/
    public ItemStack getDisplayStack();
    
    /** Called whenever this item is purchased
     *  @param      the player purchasing the item
     *  @return     return true if the gui should close after a purchase **/
    public boolean onPurchased(EntityPlayer player);

    /** Display tooltip for this item **/
    public void addTooltip(List list, EntityPlayer player);
}
