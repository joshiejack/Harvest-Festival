package joshie.harvest.api.animals;

import net.minecraft.item.ItemStack;

public interface IAnimalHandler {
    /** Call this to check if the animal can eat a specific type of food
     *  @param tracked      the animal
     *  @param type         the food type **/
    boolean canAnimalEatFoodType(IAnimalTracked tracked, AnimalFoodType type);

    /** Returns true if the item type matches any of the food types **/
    boolean canEat(ItemStack held, AnimalFoodType... types);

    /** Register an item as a specific food type **/
    void registerFoodAsType(ItemStack stack, AnimalFoodType type);
    
    /** Create a new animal data
     * @param animal    the animal
     * @param type      you can pass the string, if you want to get a registered type, e.g. cow, sheep or sheep
     * @return the data **/
    IAnimalData newData(IAnimalTracked animal, String type);
    
    /** Returns an animal type based on the string name **/
    IAnimalType getTypeFromString(String string);

    /** Register an animal type **/
    void registerType(String key, IAnimalType type);
}