package joshie.harvest.animals.type;

import joshie.harvest.api.animals.AnimalStats;
import net.minecraft.entity.passive.EntityAnimal;

import static joshie.harvest.api.animals.AnimalFoodType.GRASS;

public class AnimalSheep extends AnimalAbstract {
    public AnimalSheep() {
        super("sheep", 8, 12, GRASS);
    }

    @Override
    public int getFeedByHandBonus() {
        return 30;
    }

    @Override
    public int getOutsideBonus() {
        return 3;
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
    public void refreshProduct(AnimalStats stats, EntityAnimal entity) {
        entity.eatGrassBonus();
    }
}
