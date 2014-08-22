package harvestmoon.helpers;

import static harvestmoon.HarvestMoon.handler;
import static harvestmoon.network.PacketHandler.sendToEveryone;
import harvestmoon.network.PacketSyncCanProduce;
import net.minecraft.entity.passive.EntityAnimal;

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
}
