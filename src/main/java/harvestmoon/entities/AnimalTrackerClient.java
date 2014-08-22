package harvestmoon.entities;

import java.util.HashMap;
import java.util.UUID;

import net.minecraft.entity.passive.EntityAnimal;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class AnimalTrackerClient {
    private static HashMap<UUID, Boolean> canProduce = new HashMap();

    @SideOnly(Side.CLIENT)
    public boolean canProduceProduct(EntityAnimal animal) {
        Boolean can = canProduce.get(animal.getPersistentID());
        if (can == null) {
            canProduce.put(animal.getPersistentID(), true);
        }

        return can == null ? true : can;
    }

    @SideOnly(Side.CLIENT)
    public void setCanProduceProduct(EntityAnimal animal, boolean value) {
        canProduce.put(animal.getPersistentID(), value);
    }
    
    @SideOnly(Side.CLIENT)
    public void onDeath(EntityAnimal animal) {
        canProduce.remove(animal.getPersistentID());
    }
}
