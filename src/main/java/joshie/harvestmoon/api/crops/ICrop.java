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

    /** Return this crop in item form, with stats adjust for quality
     *  @param      the quality the stack should be adjust for
     *  @return     the itemstack */
    public ItemStack getCropStackForQuality(int quality);

    /** Returns the unlocalized name for this crop
     *  @return      the unlocalize name **/
    public String getUnlocalizedName();

    /** Returns the localized name for this crop 
     * @param       true if we are asking for the crops item name, 
     *              false if we are asking for it's block name
     *              
     * @return      the localized name*/
    public String getLocalizedName(boolean isItem);
    
    /** Returns the localized name for this seed 
     * @return      the localized name for this crop as seeds*/
    public String getSeedsName();

    /** Returns how much this crop will sell for at 0.5 start
     * @return      money gained from selling*/
    public long getSellValue();

    /** Returns how many stages this crop has 
     * @return      the total number of stages*/
    public int getStages();

    /** Whether this crop is always the same
     * 
     * @return      true if the crop has a quality/rating, false if it does not.*/
    public boolean isStatic();

    /** Returns true when the itemstack matches this crop
     * 
     * @param       the itemstack
     * @return      whether the passed in stack is this crop */
    public boolean matches(ItemStack stack);

    /** Associates this crop with the item
     * @param       item of this crop
     * @return      the instance*/
    public ICrop setItem(Item item);

    /** Associates this crop with this VisualHandler
     * @param       item of this crop
     * @return      the instance*/
    public ICrop setCropIconHandler(ICropIconHandler handler);

    /** If if you call this when creating a crop,
     *  it will use a different name for it's block and item form.
     * @return      the ICrop */
    public ICrop setHasAlternativeName();

    /** If you call this when creating a crop
     *  It will have quality to it/rating to it.
     * @return      theICop */
    public ICrop setIsStatic();
}
