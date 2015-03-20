package joshie.harvestmoon.api.cooking;

import java.util.HashSet;

public interface IMealRecipe {
    /** Returns the meal based on the input 
     * 
     * @param       the utensil
     * @param       the ingredients
     * @param       the fluid contained, can be null
     * @return      the meal returned, returns null if the recipe is not valid */
    public IMeal getMeal(IUtensil utensil, HashSet<ICookingComponent> ingredients);
    
    /** @return     the basic meal **/
    public IMeal getMeal();
    
    /** @return     the best meal **/
    public IMeal getBestMeal();

    /** Add optional ingredients as possible for a recipe
     * 
     * @param       the ingredients
     * @return      the meal */
    public IMealRecipe setOptionalIngredients(ICookingComponent... ingredients);

    /** Set the tool required for this, without setting, it defaults to none
     * 
     * @param       the utensil
     * @return      the meal  */
    public IMealRecipe setRequiredTool(IUtensil tool);
}
