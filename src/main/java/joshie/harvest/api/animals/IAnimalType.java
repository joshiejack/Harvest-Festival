package joshie.harvest.api.animals;

import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface IAnimalType {
    /** Return a simple name for this animal type **/
    String getName();
    
    /** @return an array of food type this animal can consume **/
    AnimalFoodType[] getFoodTypes();

    /** @return the minimum lifespan for this animal type **/
    int getMinLifespan();
    
    /** @return the maximum lifespan for this animal type **/
    int getMaxLifespan();

    /** @return the number of days between producing products **/
    int getDaysBetweenProduction();
    
    /** @return how many generic treats
     *  this animal needs to up it's productivity **/
    int getGenericTreatCount();
    
    /** @return how many typed treats
     *  this animal needs to up it's productivity **/
    int getTypeTreatCount();

    /** The relationship bonus based on the action
     *  @param action the action
     *  @return the points awarded**/
    int getRelationshipBonus(AnimalAction action);

    /** @return the product that this animal produces **/
    ItemStack getProduct(EntityPlayer player, AnimalStats stats);

    /** Called whenever an animal is reset to being able to produce again
     * @param stats     the stats
     * @param entity    the animal**/
    void refreshProduct(AnimalStats stats, EntityAnimal entity);
}