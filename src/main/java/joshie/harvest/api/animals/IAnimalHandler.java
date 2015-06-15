package joshie.harvest.api.animals;

import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.item.ItemStack;

public interface IAnimalHandler {
    /** Returns true if the item type matches any of the food types **/
    public boolean canEat(AnimalFoodType[] foodTypes, ItemStack held);

    /** Register an item as a specific food type **/
    public void registerFoodAsType(ItemStack stack, AnimalFoodType type);
    
    /** Creates a new animal data **/
    public IAnimalData newData(IAnimalTracked animal);
    
    /** Returns an animal type based on the string name **/
    public IAnimalType getTypeFromString(String string);

    /** Returns an animal type based on the entity **/
    public IAnimalType getType(EntityAnimal animal);

    /** Register an animal type **/
    public void registerType(String key, IAnimalType type);
}
