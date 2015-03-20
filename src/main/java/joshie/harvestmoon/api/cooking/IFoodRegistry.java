package joshie.harvestmoon.api.cooking;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;

public interface IFoodRegistry {
    /** Registers this item as a cooking component type
     *  @param      the itemstack
     *  @param      the component **/
    public void register(ItemStack stack, ICookingComponent component);

    /** Returns a set of all the components this stack provides
     * 
     * @param       the stack
     * @return      the components the stack provides*/
    public List<ICookingComponent> getCookingComponents(ItemStack stack);
    
    /** Returns a cooking component based on it's unlocalized name
     *  Cooking components are regitered when an item is registered to them,
     *  if they have no stack, then they are not registered, and therefore
     *  do not exist. And cannot be found with this method.
     *  
     *  @param      the unlocalized name of the component
     *  @return     the cooking component  */
    public ICookingComponent getComponent(String unlocalized);

    /** Returns a fluid for this ingredient if it's valid
     *  This is called by the client when rendering, to determine how to rend the item.
     *  Returns null if the ingredient doesn't have a fluid associated with it.
     * @param       the ingredient
     * @return      a fluid, can be null */
    public Fluid getFluid(ItemStack ingredient);
    
    /** Returns a utensil based on the unlocalized name
     *  Returns kitchen counter if none was found.
     * 
     * @param       unlocalized name
     * @return      the utensil */
    public IUtensil getUtensil(String unlocalized);

    /** Add a recipe for cooking a meal
     *  @param      the recipe */
    public void addRecipe(IMealRecipe recipe);

    /** Returns a resulting itemstack for the ingredients input
     * @param fluid 
     *  @param      the utensil in use
     *  @param      the ingredients
     *  @return     the resulting item */
    public ItemStack getResult(IUtensil utensil, ArrayList<ItemStack> ingredients);
    
    /** Returns a list of all the recipes 
     *  @return     the entire recipe list */
    public List<IMealRecipe> getRecipes();
    
    /** Returns a list of all the unique meals
     *  @return     the meals */
    public Set<IMeal> getMeals();
}
