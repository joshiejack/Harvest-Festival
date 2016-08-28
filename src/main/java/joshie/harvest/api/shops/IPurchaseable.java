package joshie.harvest.api.shops;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public interface IPurchaseable extends ISpecialPurchaseRules {
    /** Whether this item shows up in the shop gui for purchase **/
    boolean canList(World world, EntityPlayer player);
    
    /** The cost of this product **/
    long getCost();
    
    /** This is the itemstack that gets displayed in the shop view **/
    ItemStack getDisplayStack();
    
    /** Called whenever this item is purchased
     *  @param      player the player purchasing the item
     *  @return     return true if the gui should close after a purchase **/
    boolean onPurchased(EntityPlayer player);

    /** Display tooltip for this item **/
    @SideOnly(Side.CLIENT)
    void addTooltip(List<String> list);
}