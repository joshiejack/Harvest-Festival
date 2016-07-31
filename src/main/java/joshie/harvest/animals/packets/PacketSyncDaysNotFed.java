package joshie.harvest.animals.packets;

import joshie.harvest.api.animals.IAnimalTracked;
import joshie.harvest.core.network.Packet;
import net.minecraft.entity.player.EntityPlayer;

@Packet(Packet.Side.CLIENT)
public class PacketSyncDaysNotFed extends AbstractSyncByte {
    public PacketSyncDaysNotFed(){}
    public PacketSyncDaysNotFed(int id, byte data) {
        super(id, data);
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        IAnimalTracked entity = getAnimal();
        if (entity != null) {
            entity.getData().setDaysNotFed(data);
        }
    }
}