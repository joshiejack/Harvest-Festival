package joshie.harvest.api.cooking;

import java.util.HashSet;

public interface IMealRecipe {
    /** Returns the meal based on the input 
     * 
     * @param       utensil the utensil
     * @param       ingredients the ingredients
     * @return      the meal returned, returns null if the recipe is not valid */
    public IMeal getMeal(IUtensil utensil, HashSet<ICookingComponent> ingredients);
    
    /** @return     the basic meal **/
    public IMeal getMeal();
    
    /** @return     the best meal **/
    public IMeal getBestMeal();

    /** Add optional ingredients as possible for a recipe
     * 
     * @param       ingredients the ingredients
     * @return      the meal */
    public IMealRecipe setOptionalIngredients(ICookingComponent... ingredients);

    /** Set the tool required for this, without setting, it defaults to none
     * 
     * @param       tool the utensil
     * @return      the meal  */
    public IMealRecipe setRequiredTool(IUtensil tool);

    /** Marks the meal as something you drink, not eat **/
    public IMealRecipe setIsDrink();
    
    /** Marks the meal as having an alt texture **/
    public IMealRecipe setHasAltTexture();
}