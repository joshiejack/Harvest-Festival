package joshie.harvest.api.animals;

import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public interface IAnimalHandler {
    @CapabilityInject(AnimalStats.class)
    Capability<AnimalStats> ANIMAL_STATS_CAPABILITY = null;

    /** Call this to check if the animal can eat a specific type of food
     *  @param stats        the animal stats
     *  @param type         the food type **/
    boolean canAnimalEatFoodType(AnimalStats stats, AnimalFoodType type);

    /** Returns true if the item type matches any of the food types **/
    boolean canEat(ItemStack held, AnimalFoodType... types);

    /** Register an item as a specific food type **/
    void registerFoodAsType(ItemStack stack, AnimalFoodType type);

    /** Creates a new animal stats
     *  @param type    the animal type **/
    AnimalStats newStats(IAnimalType type);
    
    /** Returns an animal type based on the string name
     *  Default values are "cow", "chicken", "sheep"
     *  If your animal should behave the same as these,
     *  then use those, other I advise creating your own**/
    IAnimalType getTypeFromString(String string);

    /** Syncs the animal to everyone nearby,
     *  Only call this when the entity actually exists
     *  @param animal        the animal**/
    void syncAnimalStats(EntityAnimal animal);
}