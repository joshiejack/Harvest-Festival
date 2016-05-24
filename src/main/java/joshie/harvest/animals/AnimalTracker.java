package joshie.harvest.animals;

import joshie.harvest.api.animals.IAnimalData;
import joshie.harvest.api.animals.IAnimalTracked;
import joshie.harvest.core.HFTracker;

public abstract class AnimalTracker extends HFTracker {
    public void onDeath(IAnimalTracked animal) {}
    public void onJoinWorld(IAnimalData data) {}
    public void newDay() {}
}