package joshie.harvest.animals;

import java.util.HashSet;
import java.util.Iterator;

import joshie.harvest.api.animals.IAnimalData;
import joshie.harvest.api.animals.IAnimalTracked;
import joshie.harvest.core.handlers.HFTracker;
import joshie.harvest.player.PlayerTracker;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.util.DamageSource;

//Handles the Data for the crops rather than using TE Data
public class AnimalTrackerServer extends AnimalTracker {
    private static final DamageSource natural_causes = new DamageSource("natural").setDamageBypassesArmor();
    private HashSet<IAnimalData> animals = new HashSet();

    @Override
    public void onJoinWorld(IAnimalData animal) {
        animals.add(animal);
    }

    @Override
    public void onDeath(IAnimalTracked animal) {
        animals.remove(animal.getData());
        for (PlayerTracker data: HFTracker.getPlayerTrackers()) {
            data.getRelationships().clear(animal);
        }
    }

    @Override
    public void newDay() {
        Iterator<IAnimalData> iter = animals.iterator();
        while (iter.hasNext()) {
            IAnimalData data = iter.next();
            if (!data.newDay()) { //If the new day wasn't successful, remove the animal from your memory
                iter.remove();
                EntityAnimal animal = data.getAnimal();
                if (animal != null) {
                    animal.attackEntityFrom(natural_causes, 1000F);
                }
            }
        }
    }

    @Override
    public boolean canProduceProduct(IAnimalData animal) {
        return animal.canProduce();
    }

    @Override
    public void setProducedProduct(IAnimalData animal) {
        animal.setProduced();
    }
}
