package joshie.harvestmoon.api.cooking;

import net.minecraftforge.fluids.Fluid;

public interface ICookingComponent {
    /** Whether these cooking components are equal or not **/
    public boolean isEqual(ICookingComponent component);

    /** Adds this component as an equivalent item **/
    public ICookingComponent add(ICookingComponent... component);

    /** Assigns this component to the other components equivalency list **/
    public ICookingComponent assign(ICookingComponent... component);
    
    /** @return     the unlocalized name **/
    public String getUnlocalizedName();
    
    /** Assign a fluid to this ingredient, for rendering purposes
     *  @param      the fluid
     *  @return     the ingredient **/
    public ICookingComponent setFluid(Fluid fluid);
    
    /** @return     the fluid, can be null **/
    public Fluid getFluid();

    /** The following values are added to meals, when the ingredient
     *  is considered an optional ingredient. The stats boost the meals
     *  quality (or reduce, depending on the item) */
    
    /** @return     ingredients eat time **/
    public int getEatTime();

    /** @return     ingredients stamina boost **/
    public int getStamina();

    /** @return     ingredients fatigue boost **/
    public int getFatigue();

    /** @return     ingredients hunger value **/
    public int getHunger();
    
    /** @return     ingredients saturation value **/
    public float getSaturation();
}
