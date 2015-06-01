package joshie.harvest.animals;

import java.util.HashMap;
import java.util.UUID;

import joshie.harvest.core.helpers.UUIDHelper;
import net.minecraft.entity.passive.EntityAnimal;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class AnimalTrackerClient {
    private HashMap<UUID, Boolean> canProduce = new HashMap();

    public boolean canProduceProduct(EntityAnimal animal) {
        Boolean can = canProduce.get(UUIDHelper.getEntityUUID(animal));
        if (can == null) {
            canProduce.put(UUIDHelper.getEntityUUID(animal), true);
        }

        return can == null ? true : can;
    }

    public void setCanProduceProduct(EntityAnimal animal, boolean value) {
        canProduce.put(UUIDHelper.getEntityUUID(animal), value);
    }

    public void onDeath(EntityAnimal animal) {
        canProduce.remove(UUIDHelper.getEntityUUID(animal));
    }
}
