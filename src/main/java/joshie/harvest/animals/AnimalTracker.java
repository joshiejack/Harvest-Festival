package joshie.harvest.animals;

import joshie.harvest.api.animals.IAnimalData;
import joshie.harvest.api.animals.IAnimalTracked;

public abstract class AnimalTracker {
    public abstract boolean canProduceProduct(IAnimalData animal);
    public void setProducedProduct(IAnimalData animal) {};
    public void setCanProduceProduct(IAnimalData entity, boolean canProduce) {};
    public void onDeath(IAnimalTracked animal){};
    public void onJoinWorld(IAnimalData data) {};
    public void newDay() {};
}
