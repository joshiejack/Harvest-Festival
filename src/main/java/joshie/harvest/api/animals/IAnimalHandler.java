package joshie.harvest.api.animals;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

import javax.annotation.Nullable;

public interface IAnimalHandler {
    @CapabilityInject(AnimalStats.class)
    Capability<AnimalStats> ANIMAL_STATS_CAPABILITY = null;

    /** Call this to check if the animal can eat a specific type of food
     *  @param stats        the animal stats
     *  @param type         the food type **/
    boolean canAnimalEatFoodType(AnimalStats stats, AnimalFoodType type);

    /** Returns true if the item type matches any of the food types **/
    boolean canEat(ItemStack held, AnimalFoodType... types);

    /** Register an item as a specific food type
     *  @param stack    the item
     *  @param type     the type**/
    void registerFoodAsType(ItemStack stack, AnimalFoodType type);

    /** Returns an animal stats based on animal type
     *  If your animal should behave the same as these,
     *  then use those, otherwise I advise creating your own **/
    AnimalStats<NBTTagCompound> newStats(AnimalType animal);

    /** Fetch the ai to add to your own entities,
     *  these entities must have the animal stats capability and extend EntityAnimal
     *  @param animal   the entity
     *  @param type     the type of ai you want to fetch
     *  @param add      if this is true, the ai will be added to the tasks list with its default value
     *  @return the entity ai itself, or null if the ai type was't found */
    @Nullable
    EntityAIBase getEntityAI(EntityAnimal animal, AnimalAI type, boolean add);

    /** Syncs the animal to everyone nearby,
     *  Only call this when the entity actually exists
     *  @param animal        the animal**/
    void syncAnimalStats(EntityAnimal animal);

    enum AnimalType {
        POULTRY, //Chickens are this by default, this means your animals don't need to be cleaned and are able to be picked up and loved, and don't get pregnant
        LIVESTOCK, //Sheep are this by default, it means your animals need to be cleaned, and they can get pregnant
        MILKABLE //Same as livestock, except these animals can also be milked
    }

    enum AnimalAI {
        EAT, EGGS
    }
}