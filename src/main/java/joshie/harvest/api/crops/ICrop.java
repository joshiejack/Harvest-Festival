package joshie.harvest.api.crops;

import joshie.harvest.api.animals.AnimalFoodType;
import joshie.harvest.api.calendar.Season;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public interface ICrop {
    /** Returns this crop in seed form, with default stats
     *  @return     the itemstack for this crop as seeds **/
    public ItemStack getSeedStack();

    /** Return this crop in item form, with default stats
     *  @return     the itemstack for this crop as an item **/
    public ItemStack getCropStack();
    
    /** Harvested stack **/
    public ItemStack getHarvested();

    /** Returns the unlocalized name for this crop
     *  @return      the unlocalize name **/
    public String getUnlocalizedName();

    /** Returns the localized name for this crop 
     * @param       isItem true if we are asking for the crops item name,
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

    /** The stage this crop regrows **/
    public int getRegrowStage();

    /** The colour of the seed bag */
    public int getColor();
    
    /** The year this seed becomes available for purchase **/
    public int getPurchaseYear();

    /** How much this seed costs **/
    public long getSeedCost();

    /** Whether this seed is for sale at all **/
    public boolean canPurchase();

    /** Returns the type of animal food this crop is **/
    public AnimalFoodType getFoodType();

    /** The seasons this crop can grow in **/
    public Season[] getSeasons();
    
    /** Returns the render handler this crop uses **/
    public ICropRenderHandler getCropRenderHandler();
    
    /** Return the soil handler for this crop **/
    public ISoilHandler getSoilHandler();
  
    /** Whether this crop requires a sickle to be harvested **/
    public boolean requiresSickle();
    
    /** Whether this crop requires water to grow **/
    public boolean requiresWater();
    
    /** Whether or not an item was assigned to this crop yet **/
    public boolean hasItemAssigned();

    /** Whether the crop is double tall at this stage **/
    public boolean isDouble(int stage);

    /** Whether this crop grows to the side when it is fully grown
     *  Pumpkins and Melons. Returns the block that grows, otherwise returns null */
    public Block growsToSide();

    /** Returns true when the itemstack matches this crop
     * 
     * @param       stack the itemstack
     * @return      whether the passed in stack is this crop */
    public boolean matches(ItemStack stack);

    /** Associates this crop with the item
     * @param       item of this crop
     * @return      the instance*/
    public ICrop setItem(ItemStack item);

    /** Associates this crop with this VisualHandler
     * @param       handler item of this crop
     * @return      the instance*/
    public ICrop setCropIconHandler(ICropRenderHandler handler);

    /** If if you call this when creating a crop,
     *  it will use a different name for it's block and item form.
     * @return      the ICrop */
    public ICrop setHasAlternativeName();
   
    /** If you call this when creating a crop
     *  It will require a sickle to be harvested  */
    public ICrop setRequiresSickle();
    
    /** If you call this when creating a crop, 
     *  It will not need to be watered **/
    public ICrop setNoWaterRequirements();
    
    /** If you call this when creating a crop,
     *  The handler will called when trying to plant the crop,
     *  So you can specify whether this crop is allowed to be placed
     *  on this type of soil or whatever. */
    public ICrop setSoilRequirements(ISoilHandler handler);
    
    /** Sets the stage at which this crop becomes double tall **/
    public ICrop setBecomesDouble(int doubleStage);
    
    /** Associates this crop with this drop handler **/
    public ICrop setDropHandler(IDropHandler handler);
    
    /** Sets that this crop grows to the side (pumpkins and melons) **/
    public ICrop setGrowsToSide(Block block);
    
    /** Set the animal food type of this crop, Crops default to vegetable **/
    public ICrop setAnimalFoodType(AnimalFoodType type);
}