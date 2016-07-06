package joshie.harvest.animals.type;

import static joshie.harvest.api.animals.AnimalFoodType.SEED;

public class AnimalChicken extends AbstractAnimal {
    public AnimalChicken() {
        super("chicken", 3, 10, SEED);
    }

    @Override
    public int getDaysBetweenProduction() {
        return 1;
    }

    @Override
    public int getGenericTreatCount() {
        return 2;
    }

    @Override
    public int getTypeTreatCount() {
        return 29;
    }
}