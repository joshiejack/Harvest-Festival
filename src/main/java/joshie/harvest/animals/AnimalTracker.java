package joshie.harvest.animals;

import joshie.harvest.api.animals.IAnimalData;

public abstract class AnimalTracker {
    public abstract boolean canProduceProduct(IAnimalData animal);
    public void setProducedProduct(IAnimalData animal) {};
    public void setCanProduceProduct(IAnimalData entity, boolean canProduce) {};
    public void onDeath(IAnimalData animal){};
    public void onJoinWorld(IAnimalData data) {};
    public void newDay() {};
}
