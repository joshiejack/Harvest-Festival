package joshie.harvest.animals;

import joshie.harvest.api.animals.IAnimalTracked;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.helpers.EntityHelper;
import joshie.harvest.player.PlayerTracker;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.util.DamageSource;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class AnimalTrackerServer extends AnimalTracker {
    private static final DamageSource natural_causes = new DamageSource("natural").setDamageBypassesArmor();
    private final HashSet<AnimalData> animals = new HashSet<>();
    private static final Set<Runnable> queue = new HashSet<>();

    public static void addToQueue(Runnable r) {
        queue.add(r);
    }

    public static void processQueue() {
        queue.forEach(Runnable :: run);
    }

    @Override
    public void onDeath(IAnimalTracked animal) {
        animal.getData().setDead();
    }

    public void onJoinWorld(AnimalData animal) {
        animals.add(animal);
    }

    public void newDay() {
        Iterator<AnimalData> iterator = animals.iterator();
        while (iterator.hasNext()) {
            AnimalData data = iterator.next(); //Only tick animals when owner in online
            if (!data.newDay()) { //If the new day wasn't successful, remove the animal from your memory
                EntityAnimal animal = data.getAnimal();
                iterator.remove();
                if (animal != null) {
                    animal.attackEntityFrom(natural_causes, 1000F);
                    for (PlayerTracker tracker : HFTrackers.getPlayerTrackers()) {
                        tracker.getRelationships().clear(EntityHelper.getEntityUUID(animal));
                    }
                }
            }
        }
    }
}