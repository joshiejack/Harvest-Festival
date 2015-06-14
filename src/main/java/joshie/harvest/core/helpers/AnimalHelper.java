package joshie.harvest.core.helpers;

import joshie.harvest.api.animals.AnimalFoodType;
import joshie.harvest.api.animals.IAnimalTracked;

public class AnimalHelper {
    public static void newDay() {
        ServerHelper.getAnimalTracker().newDay();
    }

    public static boolean eatsGrass(IAnimalTracked animal) {
        for (AnimalFoodType type: animal.getType().getFoodTypes()) {
            if (type.equals(AnimalFoodType.GRASS)) return true;
        }
        
        return false;
    }
}
