package joshie.harvest.animals;

import joshie.harvest.api.animals.IAnimalData;
import joshie.harvest.api.animals.IAnimalTracked;
import net.minecraft.world.World;

public abstract class AnimalTracker {
    protected World world;

    public void setWorld(World world) {
        this.world = world;
    }

    public void onDeath(IAnimalTracked animal) {}
    public void onJoinWorld(IAnimalData data) {}
    public void newDay() {}
}