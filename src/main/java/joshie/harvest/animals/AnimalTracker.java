package joshie.harvest.animals;

import joshie.harvest.api.animals.IAnimalData;
import joshie.harvest.api.animals.IAnimalTracked;

public abstract class AnimalTracker {
    public void onDeath(IAnimalTracked animal) {}
    public void onJoinWorld(IAnimalData data) {}
    public void newDay() {}
}