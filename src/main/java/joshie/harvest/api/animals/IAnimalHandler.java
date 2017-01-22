package joshie.harvest.api.animals;

import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public interface IAnimalHandler {
    @CapabilityInject(AnimalStats.class)
    Capability<AnimalStats> ANIMAL_STATS_CAPABILITY = null;

    /** Call this to check if the animal can eat a specific type of food
     *  @param stats        the animal stats
     *  @param type         the food type
     *  @return if the animal can eat this food**/
    boolean canAnimalEatFoodType(AnimalStats stats, AnimalFoodType type);

    /** If the item type matches any of the food types
     *  @param held the item
     *  @param types the types
     *  @return true if it matches **/
    boolean canEat(ItemStack held, AnimalFoodType... types);

    /** Register an item as a specific food type
     *  @param stack    the item
     *  @param type     the type**/
    void registerFoodAsType(ItemStack stack, AnimalFoodType type);

    /** Returns an animal stats based on animal type
     *  If your animal should behave the same as these,
     *  then use those, otherwise I advise creating your own
     *  @param animal the animal type
     *  @return the stats based on the input type**/
    AnimalStats<NBTTagCompound> newStats(AnimalType animal);

    /** Syncs the animal to everyone nearby,
     *  Only call this when the entity actually exists
     *  @param animal        the animal**/
    void syncAnimalStats(EntityAnimal animal);

    /** The animal type enum **/
    enum AnimalType {
        POULTRY, //Chickens are this by default, this means your animals don't need to be cleaned and are able to be picked up and loved, and don't get pregnant
        LIVESTOCK, //Sheep are this by default, it means your animals need to be cleaned, and they can get pregnant
        MILKABLE //Same as livestock, except these animals can also be milked
    }
}