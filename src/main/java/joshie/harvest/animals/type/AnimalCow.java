package joshie.harvest.animals.type;

import static joshie.harvest.api.animals.AnimalFoodType.GRASS;

public class AnimalCow extends AbstractAnimal {
    public AnimalCow() {
        super("cow", 12, 20, GRASS);
    }

    @Override
    public int getDaysBetweenProduction() {
        return 1;
    }
}
