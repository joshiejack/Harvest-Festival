package joshie.harvest.api.cooking;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public interface CookingManager {
    /** Registers this item as a cooking component type
     *  @param      stack the itemstack
     *  @param      ingredient the name of the ingredient **/
    void register(ItemStack stack, Ingredient ingredient);
    
    /** Registers a special recipe handler, called before the normal
     *  recipes are ever processed
     * @param handler   the handler */
    @Deprecated //TODO: Remove in 0.7+
    void registerRecipeHandler(RecipeHandler handler);

    /** Registers a cooking handler
     * @param handler   the handler */
    void registerCookingHandler(CookingHandler handler);

    /** Call this if you don't wish to implement IKnife,
     *  Use OreDictionary.WILDCARD_VALUE if metadata doesn't matter
     * @param stack the knife stack */
    void registerKnife(ItemStack stack);

    @Deprecated //TODO: Remove in 0.7+
    Meal addMeal(ResourceLocation key, Utensil utensil, int hunger, float saturation, int eatTimer, Ingredient... ingredients);

    @Deprecated //TODO: Remove in 0.7+
    void addRecipe(ItemStack output, Utensil utensil, Ingredient... ingredients);

    /** Add a recipe, with a custom stack output
     *  Use case for such thing is wheat > bread
     * @param key    the registry name
     * @param utensil   the utensil
     * @param output    the resulting item
     * @param ingredients the ingredients */
    Recipe addBasicRecipe(ResourceLocation key, Utensil utensil, ItemStack output, IngredientStack... ingredients);

    /** Returns true if this stack is an ingredient
     * @param  stack    the stack to validate **/
    boolean isIngredient(ItemStack stack);

    /** Returns true if this stack is considered a knife
     *  @param stack    the stack to validate */
    boolean isKnife(ItemStack stack);

    /** Returns a fluid for this ingredient if it's valid
     *  This is called by the client when rendering, to determine how to rend the item.
     *  Returns null if the ingredient doesn't have a fluid associated with it.
     * @param       ingredient the ingredient
     * @return      a fluid, can be null */
    ResourceLocation getFluid(ItemStack ingredient);

    @Deprecated //TODO: Remove in 0.7+
    ItemStack getResult(Utensil utensil, List<ItemStack> ingredients);

    /** Returns a resulting itemstack for the ingredients input
     *  @param      utensil the utensil in use
     *  @param      ingredients the ingredients
     *  @return     the resulting item */
    List<ItemStack> getCookingResult(Utensil utensil, List<ItemStack> ingredients);

    /** Returns a default copy of this meal
     *  @param name     this is the resource path of the name, if it's a harvestfestival meal you can just use the name,
     *                  if it's added by other mods you should use the normal resourcepath **/
    ItemStack getMeal(String name);
    
    /** Returns a copy of this meal, with it's best stats
     *  Can and will return null if it was not found.
     *  @param name     this is the resource path of the name, if it's a harvestfestival meal you can just use the name,
     *                  if it's added by other mods you should use the normal resourcepath **/
    ItemStack getBestMeal(String name);
}
