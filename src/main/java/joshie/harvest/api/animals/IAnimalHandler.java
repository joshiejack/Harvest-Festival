package joshie.harvest.api.animals;

import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.item.ItemStack;

public interface IAnimalHandler {
    /** Returns true if the item type matches any of the food types **/
    boolean canEat(ItemStack held, AnimalFoodType... types);

    /** Register an item as a specific food type **/
    void registerFoodAsType(ItemStack stack, AnimalFoodType type);
    
    /** Creates a new animal data **/
    IAnimalData newData(IAnimalTracked animal);
    
    /** Returns an animal type based on the string name **/
    IAnimalType getTypeFromString(String string);

    /** Returns an animal type based on the entity **/
    IAnimalType getType(EntityAnimal animal);

    /** Register an animal type **/
    void registerType(String key, IAnimalType type);
}