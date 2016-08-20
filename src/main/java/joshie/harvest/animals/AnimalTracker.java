package joshie.harvest.animals;

import joshie.harvest.api.animals.IAnimalTracked;
import joshie.harvest.core.HFTracker;

public abstract class AnimalTracker extends HFTracker {
    public abstract void onDeath(IAnimalTracked animal);
}