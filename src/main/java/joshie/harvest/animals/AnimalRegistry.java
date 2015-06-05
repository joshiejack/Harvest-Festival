package joshie.harvest.animals;

import joshie.harvest.animals.type.AnimalChicken;
import joshie.harvest.animals.type.AnimalCow;
import joshie.harvest.animals.type.AnimalSheep;
import joshie.harvest.api.animals.IAnimalData;
import joshie.harvest.api.animals.IAnimalHandler;
import joshie.harvest.api.animals.IAnimalTracked;
import joshie.harvest.api.animals.IAnimalType;
import joshie.harvest.items.ItemTreat;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntitySheep;

public class AnimalRegistry implements IAnimalHandler {
    public static final IAnimalType cow = new AnimalCow();
    public static final IAnimalType sheep = new AnimalSheep();
    public static final IAnimalType chicken = new AnimalChicken();

    @Override
    public IAnimalData newData(IAnimalTracked animal) {
        return new AnimalData(animal);
    }

    @Override
    public IAnimalType getTypeFromString(String string) {
        return string.equals("cow") ? cow : string.equals("sheep") ? sheep : string.equals("chicken") ? chicken : null;
    }

    @Override
    public IAnimalType getType(EntityAnimal animal) {
        if (animal instanceof EntityCow) {
            return cow;
        } else if (animal instanceof EntitySheep) {
            return sheep;
        } else if (animal instanceof EntityChicken) {
            return chicken;
        } else return null;
    }
}
