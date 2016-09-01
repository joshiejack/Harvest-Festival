package joshie.harvest.api.animals;

import net.minecraft.entity.passive.EntityAnimal;

public interface IAnimalType {
    /** Return a simple name for this animal type **/
    String getName();
    
    /** Return an array of food type this animal can consume **/
    AnimalFoodType[] getFoodTypes();

    /** Return the minimum lifespan for this animal type **/
    int getMinLifespan();
    
    /** Return the maximum lifespan for this animal type **/
    int getMaxLifespan();

    /** Return the number of days between producing products **/
    int getDaysBetweenProduction();
    
    /** How many generic treats this animal needs to up it's productivity **/
    int getGenericTreatCount();
    
    /** How many typed treats this animal needs to up it's productivity **/
    int getTypeTreatCount();

    /** Called whenever an animal is reset to being able to produce again
     * @param data tracking **/
    void refreshProduct(IAnimalData data, EntityAnimal entity);
}