package joshie.harvest.api.cooking;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;


public interface IMeal {
    /** Marks the meal as a drink 
     * @return **/
    IMeal setIsDrink();

    /** Gives this meal a custom resource location **/
    IMeal setResourceLocation(ResourceLocation resource);
    
    /** @return     the unlocalized name, must be unique*/
    String getUnlocalizedName();

    /** @return     how much hunger the meal restores **/
    int getHunger();

    /** @return     how much saturation the meal provides **/
    float getSaturation();

    /** @return     how much stamina the meal adds **/
    int getStamina();

    /** @return     how much fatigue the meal adds **/
    int getFatigue();

    /** @return     whether the meal is drunk (rather than eaten) **/
    boolean isDrink();

    /** @return     how long the meal takes to eat **/
    int getEatTime();
    
    /** @return     the maximum hunger this meal can provide (including optional ingredients) **/
    int getHungerCap();
    
    /** @return     the maximum saturation this meal can provide (including optional ingredients) **/
    float getSaturationCap();

    /** Adds extra data for the ingredients to this meal
     *  @param      the cooking component we're adding as an ingredient
     *  @return     the adjusted meal **/
    IMeal addIngredient(ICookingComponent optional);

    /** Returns the itemstack for this meal **/
    ItemStack cook(IMeal meal);
    
    /** Set this mean as having a different texture in utensils **/
    IMeal setAlternativeItem(ItemStack stack);

    ItemStack getAlternativeItem();

    /** Return the resource location for this meal **/
    ResourceLocation getResource();
}
