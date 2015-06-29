package joshie.harvest.animals.type;

import static joshie.harvest.api.animals.AnimalFoodType.GRASS;
import joshie.harvest.api.animals.IAnimalData;
import net.minecraft.entity.passive.EntityAnimal;

public class AnimalSheep extends AbstractAnimal {  
    public AnimalSheep() {
        super("sheep", 8, 12, GRASS);
    }

    @Override
    public int getDaysBetweenProduction() {
        return 7;
    }

    @Override
    public int getGenericTreatCount() {
        return 2;
    }

    @Override
    public int getTypeTreatCount() {
        return 29;
    }

    @Override
    public void newDay(IAnimalData data, EntityAnimal entity) {
        entity.eatGrassBonus();
    }
}
