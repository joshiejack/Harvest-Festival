package joshie.harvest.api.animals;

import net.minecraft.entity.passive.EntityAnimal;

public interface IAnimalHandler {
    /** Creates a new animal data **/
    public IAnimalData newData(IAnimalTracked animal);
    
    /** Returns an animal type based on the string name **/
    public IAnimalType getTypeFromString(String string);

    /** Returns an animal type based on the entity **/
    public IAnimalType getType(EntityAnimal animal);
}
