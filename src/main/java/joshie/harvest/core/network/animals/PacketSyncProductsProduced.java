package joshie.harvest.core.network.animals;

import joshie.harvest.api.animals.IAnimalTracked;
import net.minecraft.entity.player.EntityPlayer;

public class PacketSyncProductsProduced extends AbstractSyncBoolean {
    public PacketSyncProductsProduced() {}
    public PacketSyncProductsProduced(int id, boolean data) {
        super(id, data);
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        IAnimalTracked entity = getAnimal();
        if (entity != null) {
            entity.getData().setProductsProduced(data);
        }
    }
}