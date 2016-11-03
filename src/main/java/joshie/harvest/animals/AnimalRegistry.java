package joshie.harvest.animals;

import joshie.harvest.animals.entity.ai.EntityAIEat;
import joshie.harvest.animals.entity.ai.EntityAIEatLivestock;
import joshie.harvest.animals.entity.ai.EntityAILayEgg;
import joshie.harvest.animals.packet.PacketSyncAnimal;
import joshie.harvest.animals.stats.AnimalStatsHF;
import joshie.harvest.animals.stats.AnimalStatsLivestock;
import joshie.harvest.animals.stats.AnimalStatsMilkable;
import joshie.harvest.animals.stats.AnimalStatsPoultry;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.animals.AnimalFoodType;
import joshie.harvest.api.animals.AnimalStats;
import joshie.harvest.api.animals.IAnimalHandler;
import joshie.harvest.core.helpers.EntityHelper;
import joshie.harvest.core.network.PacketHandler;
import joshie.harvest.core.util.annotations.HFApiImplementation;
import joshie.harvest.core.util.holders.HolderRegistry;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;

@HFApiImplementation
public class AnimalRegistry implements IAnimalHandler {
    public static final AnimalRegistry INSTANCE = new AnimalRegistry();
    private final HolderRegistry<AnimalFoodType> registry = new HolderRegistry<>();

    private AnimalRegistry() {}

    //Internal Convenience method
    static void registerFoodsAsType(AnimalFoodType type, Item... items) {
        for (Item item : items) {
            HFApi.animals.registerFoodAsType(new ItemStack(item, 1, OreDictionary.WILDCARD_VALUE), type);
        }
    }

    @Override
    public void registerFoodAsType(ItemStack stack, AnimalFoodType type) {
        registry.register(stack, type);
    }

    @Override
    public boolean canAnimalEatFoodType(AnimalStats stats, AnimalFoodType type) {
        for (AnimalFoodType t: stats.getType().getFoodTypes()) {
            if (t == type) return true;
        }

        return false;
    }

    @Override
    public boolean canEat(ItemStack stack, AnimalFoodType... types) {
        for (AnimalFoodType type: types) {
            if (registry.matches(stack, type)) return true;
        }

        return false;
    }

    @Override
    public AnimalStats<NBTTagCompound> newStats(AnimalType type) {
        if (type == AnimalType.POULTRY) return new AnimalStatsPoultry();
        else if (type == AnimalType.LIVESTOCK) return new AnimalStatsLivestock();
        else if (type == AnimalType.MILKABLE) return new AnimalStatsMilkable();
        else return new AnimalStatsHF();
    }

    @Override
    public EntityAIBase getEntityAI(EntityAnimal animal, AnimalAI type, boolean add) {
        if (type == AnimalAI.EAT) {
            EntityAIBase eat = new EntityAIEat(animal);
            if (add) {
                animal.tasks.addTask(5, eat);
            }

            return eat;
        } else if (type == AnimalAI.EGGS) {
            EntityAILayEgg eat = new EntityAILayEgg(animal);
            if (add) {
                animal.tasks.addTask(5, eat);
            }

            return eat;
        } else if (type == AnimalAI.EAT_GRASS) {
            EntityAIBase eat = new EntityAIEatLivestock(animal);
            if (add) {
                animal.tasks.addTask(5, eat);
            }

            return eat;
        }

        return null;
    }

    @Override
    public void syncAnimalStats(EntityAnimal animal) {
        AnimalStats stats = EntityHelper.getStats(animal);
        if (stats != null) {
            PacketHandler.sendToAllAround(new PacketSyncAnimal(animal.getEntityId(), stats), animal.dimension, animal.posX, animal.posY, animal.posZ, 178);
        }
    }
}