package joshie.harvest.api.cooking;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.List;

public interface CookingManager {
    /** Registers this item as a cooking component type
     *  @param      stack the itemstack
     *  @param      ingredient the name of the ingredient **/
    void register(@Nonnull ItemStack stack, Ingredient ingredient);
    
    /** Registers a cooking handler
     * @param handler   the handler */
    void registerCookingHandler(CookingHandler handler);

    /** Call this if you don't wish to implement IKnife,
     *  Use OreDictionary.WILDCARD_VALUE if metadata doesn't matter
     * @param stack the knife stack */
    void registerKnife(@Nonnull ItemStack stack);

    /** Returns true if this stack is an ingredient
     * @param  stack    the stack to validate **/
    boolean isIngredient(@Nonnull ItemStack stack);

    /** Returns true if this stack is considered a knife
     *  @param stack    the stack to validate */
    boolean isKnife(@Nonnull ItemStack stack);

    /** Returns a fluid for this ingredient if it's valid
     *  This is called by the client when rendering, to determine how to rend the item.
     *  Returns null if the ingredient doesn't have a fluid associated with it.
     * @param       ingredient the ingredient
     * @return      a fluid, can be null */
    ResourceLocation getFluid(@Nonnull ItemStack ingredient);

    /** Returns a resulting itemstack for the ingredients input
     *  @param      utensil the utensil in use
     *  @param      ingredients the ingredients
     *  @return     the resulting item */
    NonNullList<ItemStack> getCookingResult(Utensil utensil, NonNullList<ItemStack> ingredients);

    /** Returns a default copy of this meal
     *  @param name     this is the resource path of the name, if it's a harvestfestival meal you can just use the name,
     *                  if it's added by other mods you should use the normal resourcepath **/
    @SuppressWarnings("unused")
    @Nonnull
    ItemStack getMeal(String name);

    /** Returns a copy of this meal, with it's best stats
     *  Can and will return null if it was not found.
     *  @param name     this is the resource path of the name, if it's a harvestfestival meal you can just use the name,
     *                  if it's added by other mods you should use the normal resourcepath **/
    @Nonnull
    ItemStack getBestMeal(String name);
}