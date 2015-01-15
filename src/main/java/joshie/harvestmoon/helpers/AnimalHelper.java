package joshie.harvestmoon.helpers;

import static joshie.harvestmoon.HarvestMoon.handler;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;

public class AnimalHelper {
    public static boolean canProduceProduct(EntityAnimal animal) {
        if (animal.worldObj.isRemote) {
            return handler.getClient().getAnimalTracker().canProduceProduct(animal);
        } else return handler.getServer().getAnimalTracker().canProduceProduct(animal);
    }

    //Increase product produced, then update the client with the new information
    public static void setProducedProduct(EntityAnimal animal) {
        if (!animal.worldObj.isRemote) {
            handler.getServer().getAnimalTracker().setProducedProduct(animal);
        }
    }

    /** Causes the player to feed the animal, affecting it's relationship **/
    public static void feed(EntityPlayer player, EntityAnimal animal) {
        if (!player.worldObj.isRemote) {
            if (handler.getServer().getAnimalTracker().setFed(animal)) {
                handler.getServer().getPlayerData(player).affectRelationship(animal, 5);
            }
        }
    }
}
