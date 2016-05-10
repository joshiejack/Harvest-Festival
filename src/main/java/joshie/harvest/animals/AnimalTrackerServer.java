package joshie.harvest.animals;

import joshie.harvest.api.animals.IAnimalData;
import joshie.harvest.api.animals.IAnimalTracked;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.player.PlayerTracker;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.util.DamageSource;

import java.util.HashSet;
import java.util.Iterator;

//Handles the Data for the crops rather than using TE Data
public class AnimalTrackerServer extends AnimalTracker {
    private static final DamageSource natural_causes = new DamageSource("natural").setDamageBypassesArmor();
    private HashSet<IAnimalData> animals = new HashSet<IAnimalData>();

    @Override
    public void onJoinWorld(IAnimalData animal) {
        animals.add(animal);
    }

    @Override
    public void onDeath(IAnimalTracked animal) {
        animals.remove(animal.getData());
        for (PlayerTracker tracker : HFTrackers.getPlayerTrackers()) {
            tracker.getRelationships().clear(animal);
        }
    }

    @Override
    public void newDay() {
        Iterator<IAnimalData> iterator = animals.iterator();
        while (iterator.hasNext()) {
            IAnimalData data = iterator.next();
            if (!data.newDay()) { //If the new day wasn't successful, remove the animal from your memory
                iterator.remove();
                EntityAnimal animal = data.getAnimal();
                if (animal != null) {
                    animal.attackEntityFrom(natural_causes, 1000F);
                }
            }
        }
    }
}