package joshie.harvest.animals.type;

import joshie.harvest.api.animals.AnimalAction;
import joshie.harvest.api.animals.AnimalFoodType;

public abstract class AnimalLivestock extends AnimalAbstract {
    public AnimalLivestock(String name, int min, int max, AnimalFoodType... types) {
        super(name, min, max, types);
    }

    @Override
    public int getRelationshipBonus(AnimalAction action) {
        switch (action) {
            case CLEAN:      return 30;
            case IMPREGNATE: return 0;
        }

        return super.getRelationshipBonus(action);
    }
}