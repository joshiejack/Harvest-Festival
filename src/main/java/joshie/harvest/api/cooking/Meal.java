package joshie.harvest.api.cooking;

import net.minecraft.item.ItemStack;

public interface Meal {
    /** Add optional ingredients as possible for a recipe
     * 
     * @param       ingredients the ingredients
     * @return      the meal */
    Meal setOptionalIngredients(Ingredient... ingredients);

    /** Set the tool required for this, without setting, it defaults to none
     * 
     * @param       tool the utensil
     * @return      the meal  */
    Meal setRequiredTool(Utensil tool);

    /** Marks the meal as something you drink, not eat **/
    Meal setIsDrink();
    
    /** Marks the meal as having an alt texture
     * @param stack the item to appear as **/
    Meal setAlternativeTexture(ItemStack stack);
}