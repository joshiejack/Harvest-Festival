package joshie.harvest.api.cooking;

import net.minecraft.util.ResourceLocation;

public interface ICookingIngredient {
    /** Whether these cooking components are equal or not **/
    boolean isEqual(ICookingIngredient component);

    /** Adds this component as an equivalent item **/
    ICookingIngredient add(ICookingIngredient... component);

    /** Assigns this component to the other components equivalency list **/
    ICookingIngredient assign(ICookingIngredient... component);
    
    /** @return     the unlocalized name **/
    String getUnlocalizedName();
    
    /** Assign a fluid to this ingredient, for rendering purposes
     *  @param      fluid the fluid
     *  @return     the ingredient **/
    ICookingIngredient setFluid(ResourceLocation fluid);
    
    /** @return     the fluid, can be null **/
    ResourceLocation getFluid();

    /** The following values are added to meals, when the ingredient
     *  is considered an optional ingredient. The stats boost the meals
     *  score (or reduce, depending on the item) */
    
    /** @return     ingredients eat time **/
    int getEatTime();

    /** @return     ingredients stamina boost **/
    int getStamina();

    /** @return     ingredients fatigue boost **/
    int getFatigue();

    /** @return     ingredients hunger value **/
    int getHunger();
    
    /** @return     ingredients saturation value **/
    float getSaturation();
}