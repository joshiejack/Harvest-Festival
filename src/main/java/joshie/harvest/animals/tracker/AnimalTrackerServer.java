package joshie.harvest.animals.tracker;

import joshie.harvest.api.animals.AnimalStats;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class AnimalTrackerServer extends AnimalTracker {
    private static final DamageSource natural_causes = new DamageSource("natural").setDamageBypassesArmor();
    private final Set<AnimalStats> animals = new HashSet<>();
    private final Set<AnimalStats> queue = new HashSet<>();

    public void add(AnimalStats stats) {
        queue.add(stats);
    }

    public void processQueue() {
        animals.addAll(queue);
        queue.clear(); //Clear the queue
    }

    public void onDeath(AnimalStats stats) {
        stats.setDead();
    }

    public void biHourly() {
        try {
            animals.stream().forEach(AnimalStats::onBihourlyTick);
        } catch (ConcurrentModificationException ex) { /**/ }
    }

    public void newDay() {
        World world = getWorld();
        Iterator<AnimalStats> iterator = animals.iterator();
        while (iterator.hasNext()) {
            AnimalStats data = iterator.next(); //Only tick animals when owner in online
            EntityAnimal animal = data.getAnimal();
            if (animal != null && world.loadedEntityList.contains(animal)) {
                if (animal.isDead || !data.newDay()) { //If the new day wasn't successful, remove the animal from your memory
                    iterator.remove();
                    if (!animal.isDead) {
                        animal.attackEntityFrom(natural_causes, 1000F);
                    }
                }
            }
        }
    }
}