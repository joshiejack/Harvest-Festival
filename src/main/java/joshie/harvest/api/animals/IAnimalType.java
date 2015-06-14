package joshie.harvest.api.animals;

import net.minecraft.entity.passive.EntityAnimal;

public interface IAnimalType {
    /** Return a simple name for this animal type **/
    public String getName();
    
    /** Return an array of food type this animal can consume **/
    public AnimalFoodType[] getFoodTypes();

    /** Return the minimum lifespan for this animal type **/
    public int getMinLifespan();
    
    /** Return the maximum lifespan for this animal type **/
    public int getMaxLifespan();

    /** Return the number of days between producing products **/
    public int getDaysBetweenProduction();

    /** Called to make this animal produce it a product on a new day 
     * @param tracking **/
    public void newDay(IAnimalData data, EntityAnimal entity);
}
