package joshie.harvest.animals.tracker;

import joshie.harvest.api.animals.AnimalStats;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.helpers.EntityHelper;
import joshie.harvest.player.PlayerTracker;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.util.DamageSource;

import java.util.*;
import java.util.Map.Entry;

public class AnimalTrackerServer extends AnimalTracker {
    private static final DamageSource natural_causes = new DamageSource("natural").setDamageBypassesArmor();
    private final HashMap<EntityAnimal, AnimalStats> animals = new HashMap<>();
    private static final Set<Runnable> queue = new HashSet<>();

    public static void addToQueue(Runnable r) {
        queue.add(r);
    }

    public static void processQueue() {
        Set<Runnable> toProcess = new HashSet<>(queue);
        queue.clear(); //Clear the old queue
        toProcess.forEach(Runnable :: run);
    }

    @Override
    public void onDeath(AnimalStats stats) {
        stats.setDead();
    }

    public void onJoinWorld(EntityAnimal entity, AnimalStats animal) {
        animals.put(entity, animal);
    }

    public void biHourly() {
        try {
            for (AnimalStats stats : animals.values()) {
                stats.onBihourlyTick();
            }
        } catch (ConcurrentModificationException ex) { /**/ }
    }

    public void newDay() {
        Iterator<Entry<EntityAnimal, AnimalStats>> iterator = animals.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<EntityAnimal, AnimalStats> data = iterator.next(); //Only tick animals when owner in online
            EntityAnimal animal = data.getKey();
            if (animal == null || animal.isDead || !data.getValue().newDay()) { //If the new day wasn't successful, remove the animal from your memory
                iterator.remove();
                if (animal != null && !animal.isDead) {
                    animal.attackEntityFrom(natural_causes, 1000F);
                    for (PlayerTracker tracker : HFTrackers.getPlayerTrackers()) {
                        tracker.getRelationships().clear(EntityHelper.getEntityUUID(animal));
                    }
                }
            }
        }
    }
}