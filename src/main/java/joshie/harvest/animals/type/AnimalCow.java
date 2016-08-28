package joshie.harvest.animals.type;

import static joshie.harvest.api.animals.AnimalFoodType.GRASS;

public class AnimalCow extends AnimalAbstract {
    public AnimalCow() {
        super("cow", 12, 20, GRASS);
    }

    @Override
    public int getDaysBetweenProduction() {
        return 1;
    }

    @Override
    public int getGenericTreatCount() {
        return 7;
    }

    @Override
    public int getTypeTreatCount() {
        return 24;
    }
}
