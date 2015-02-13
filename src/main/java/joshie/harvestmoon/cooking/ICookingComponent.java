package joshie.harvestmoon.cooking;


public interface ICookingComponent {    
    /** Used for displaying this in the recipe book **/
    public String getUnlocalizedName();

    /** Whether these cooking components are equal or not **/
    public boolean isEqual(ICookingComponent component);
    
    /** Adds this component as an equivalent item **/
    public ICookingComponent add(ICookingComponent component);
    
    /** Assigns this component to the other components equivalency list **/
    public ICookingComponent assign(ICookingComponent component);
}
