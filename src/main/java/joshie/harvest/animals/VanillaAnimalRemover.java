package joshie.harvest.animals;

import joshie.harvest.core.util.HFLoader;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.SpawnListEntry;

import java.util.Iterator;

@HFLoader
public class VanillaAnimalRemover {
    /* Disables vanilla cows, chickens and sheep from spawning naturally if spawning is disabled **/
    public static void complete() {
        if (!HFAnimals.CAN_SPAWN) {
            for (Biome biome: Biome.REGISTRY) {
                Iterator<SpawnListEntry> it = biome.getSpawnableList(EnumCreatureType.CREATURE).iterator();
                while (it.hasNext()) {
                    SpawnListEntry entry = it.next();
                    if (entry.entityClass == EntityCow.class || entry.entityClass == EntityChicken.class || entry.entityClass == EntitySheep.class) {
                        it.remove();
                    }
                }
            }
        }
    }
}
