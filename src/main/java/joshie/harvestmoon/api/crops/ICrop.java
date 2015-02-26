package joshie.harvestmoon.api.crops;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public interface ICrop {
    /** Returns this crop in seed form, with default stats
     *  @return     the itemstack for this crop as seeds **/
    public ItemStack getSeedStack();

    /** Return this crop in item form, with default stats
     *  @return     the itemstack for this crop as an item **/
    public ItemStack getCropStack();

    /** Returns the unlocalized name for this crop
     *  @return      the unlocalize name **/
    public String getUnlocalizedName();

    /** Returns the localized name for this crop 
     * @param       true if we are asking for the crops item name, 
     *              false if we are asking for it's block name
     *              
     * @return      the localized name*/
    public String getCropName(boolean isItem);

    /** Returns how much this crop will sell for at 0.5 start
     * @return      money gained from selling*/
    public long getSellValue();

    /** Whether this crop is always the same
     * 
     * @return      true if the crop has a quality/rating, false if it does not.*/
    public boolean isStatic();

    /** Associates this crop with the item
     * @param       item of this crop
     * @return      the instance*/
    public ICrop setItem(Item item);

    /** Returns true when the itemstack matches this crop
     * 
     * @param       the itemstack
     * @return      whether the passed in stack is this crop */
    public boolean matches(ItemStack stack);
}
