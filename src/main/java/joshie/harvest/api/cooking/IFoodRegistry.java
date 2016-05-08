package joshie.harvest.api.cooking;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;

import java.util.Collection;
import java.util.List;

public interface IFoodRegistry {
    /** Registers this item as a cooking component type
     *  @param      stack the itemstack
     *  @param      component the component **/
    public void register(ItemStack stack, ICookingComponent component);
    
    /** Registers a special recipe handler, called before the normal
     *  recipes are ever processed
     * @param handler */
    public void registerRecipeHandler(ISpecialRecipeHandler handler);

    /** Returns a set of all the components this stack provides
     * 
     * @param       stack the stack
     * @return      the components the stack provides*/
    public List<ICookingComponent> getCookingComponents(ItemStack stack);
    
    /** Returns a cooking component based on it's unlocalized name
     *  Cooking components are regitered when an item is registered to them,
     *  if they have no stack, then they are not registered, and therefore
     *  do not exist. And cannot be found with this method.
     *  
     *  @param      unlocalized the unlocalized name of the component
     *  @return     the cooking component  */
    public ICookingComponent getComponent(String unlocalized);
    
    /** Creates a new cooking category, e.g. "fruit"
     *  To add things to this category, simple call 
     *  newCategory("fruit").add(apple, banana, pineapple);
     *  You could recreate this with the newIngredient
     *  by setting stats to 0, but this is for convenience.
     * @param       unlocalized name
     * @return      the component
     */
    public ICookingComponent newCategory(String unlocalized);
    
    /** Creates a new ingredient type for usage in cooking
     *  @param      unlocalised the unlocalised name, this needs to be unique
     *          The food stats are how much this ingredient affects recipes
     *          when it gets added to them as optional ingredients;
     *  @param      stamina the stamina this ingredient restores
     *  @param      fatigue the fatigue this ingredient adds (use negative to remove fatigue)
     *  @param      hunger the hunger (vanilla) this ingredient fills
     *  @param      saturation the saturation (vanilla) this ingredient fills
     *  @param      eatTimer the eatTimer, this is how many ticks extra this adds to eating time **/
    public ICookingComponent newIngredient(String unlocalised, int stamina, int fatigue, int hunger, float saturation, int eatTimer);

    /** Returns a fluid for this ingredient if it's valid
     *  This is called by the client when rendering, to determine how to rend the item.
     *  Returns null if the ingredient doesn't have a fluid associated with it.
     * @param       ingredient the ingredient
     * @return      a fluid, can be null */
    public Fluid getFluid(ItemStack ingredient);
    
    /** Returns a utensil based on the unlocalized name
     *  Returns kitchen counter if none was found.
     * 
     * @param       unlocalized name
     * @return      the utensil */
    public IUtensil getUtensil(String unlocalized);

    /** Add a recipe for cooking a meal
     *  @param      recipe the recipe
     * @return */
    public IMealRecipe addRecipe(IMealRecipe recipe);

    /** Returns a resulting itemstack for the ingredients input
     *  @param      utensil the utensil in use
     *  @param      ingredients the ingredients
     *  @return     the resulting item */
    public ItemStack getResult(IUtensil utensil, List<ItemStack> ingredients);
    
    /** Returns a collection of all the recipes
     *  @return     the entire recipe list */
    public Collection<IMealRecipe> getRecipes();

    /** Returns a default copy of this meal **/
    public ItemStack getMeal(String string);
    
    /** Returns a copy of this meal, with it's best stats
     *  Can and will return null if it was not found. **/
    public ItemStack getBestMeal(String string);

    /** Get the core recipe by name **/
    IMealRecipe getRecipe(String meal);
}
