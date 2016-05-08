package joshie.harvest.core.network.animals;

import joshie.harvest.api.animals.IAnimalTracked;
import net.minecraft.entity.player.EntityPlayer;

public class PacketSyncHealthiness extends AbstractSyncByte {
    public PacketSyncHealthiness(){}
    public PacketSyncHealthiness(int id, byte data) {
        super(id, data);
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        IAnimalTracked entity = getAnimal();
        if (entity != null) {
            entity.getData().setHealthiness(data);
        }
    }
}