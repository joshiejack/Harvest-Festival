package joshie.harvest.animals.type;

import static joshie.harvest.api.animals.AnimalFoodType.GRASS;
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
    public void newDay(EntityAnimal entity) {
        entity.eatGrassBonus();
    }
}
