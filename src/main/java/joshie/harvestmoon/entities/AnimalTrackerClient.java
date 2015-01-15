package joshie.harvestmoon.entities;

import java.util.HashMap;
import java.util.UUID;

import net.minecraft.entity.passive.EntityAnimal;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class AnimalTrackerClient {
    private HashMap<UUID, Boolean> canProduce = new HashMap();

    public boolean canProduceProduct(EntityAnimal animal) {
        Boolean can = canProduce.get(animal.getPersistentID());
        if (can == null) {
            canProduce.put(animal.getPersistentID(), true);
        }

        return can == null ? true : can;
    }

    public void setCanProduceProduct(EntityAnimal animal, boolean value) {
        canProduce.put(animal.getPersistentID(), value);
    }

    public void onDeath(EntityAnimal animal) {
        canProduce.remove(animal.getPersistentID());
    }
}
