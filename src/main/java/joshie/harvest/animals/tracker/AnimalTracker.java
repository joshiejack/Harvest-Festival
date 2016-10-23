package joshie.harvest.animals.tracker;

import joshie.harvest.api.animals.AnimalStats;
import joshie.harvest.core.util.HFTracker;

public abstract class AnimalTracker extends HFTracker {
    public abstract void onDeath(AnimalStats animal);
}