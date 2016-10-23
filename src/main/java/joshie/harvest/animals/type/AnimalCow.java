package joshie.harvest.animals.type;

import static joshie.harvest.api.animals.AnimalFoodType.GRASS;

public class AnimalCow extends AnimalAbstract {
    public AnimalCow() {
        super("cow", 12, 20, GRASS);
    }

    @Override
    public int getFeedByHandBonus() {
        return 10;
    }

    @Override
    public int getOutsideBonus() {
        return 2;
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
