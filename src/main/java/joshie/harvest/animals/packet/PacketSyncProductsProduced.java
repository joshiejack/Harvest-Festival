package joshie.harvest.animals.packet;

import joshie.harvest.animals.AnimalData;
import joshie.harvest.api.animals.IAnimalTracked;
import joshie.harvest.core.network.Packet;
import net.minecraft.entity.player.EntityPlayer;

@Packet(Packet.Side.CLIENT)
public class PacketSyncProductsProduced extends AbstractSyncByte {
    public PacketSyncProductsProduced() {}
    public PacketSyncProductsProduced(int id, int data) {
        super(id, (byte) data);
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        IAnimalTracked entity = getAnimal();
        if (entity != null) {
            AnimalData theData = (AnimalData) entity.getData();
            theData.setProductsProduced(data);
        }
    }
}