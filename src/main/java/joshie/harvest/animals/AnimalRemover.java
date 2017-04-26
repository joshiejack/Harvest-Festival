package joshie.harvest.animals;

import joshie.harvest.core.util.annotations.HFLoader;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.world.biome.Biome;

@HFLoader
public class AnimalRemover {
    /* Disables vanilla cows, chickens and sheep from spawning naturally if spawning is disabled **/
    public static void complete() {
        if (!HFAnimals.CAN_SPAWN) {
            for (Biome biome: Biome.REGISTRY) {
                biome.getSpawnableList(EnumCreatureType.CREATURE).removeIf(entry -> entry.entityClass == EntityCow.class || entry.entityClass == EntityChicken.class || entry.entityClass == EntitySheep.class);
            }
        }
    }
}
