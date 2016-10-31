package joshie.harvest.api.cooking;

import net.minecraft.item.ItemStack;

@Deprecated
public interface Meal {
    /** Add optional ingredients as possible for a recipe
     * 
     * @param       ingredients the ingredients
     * @return      the meal */
    Meal setOptionalIngredients(Ingredient... ingredients);

    /** Marks the meal as something you drink, not eat **/
    Meal setIsDrink();
    
    /** Marks the meal as having an alt texture
     * @param stack the item to appear as **/
    Meal setAlternativeTexture(ItemStack stack);

    /** Set the exhaustion level of the meal, defaults to not adding any
     *
     * @param       exhaustion the value
     * @return      the meal */
    Meal setExhaustion(float exhaustion);
}