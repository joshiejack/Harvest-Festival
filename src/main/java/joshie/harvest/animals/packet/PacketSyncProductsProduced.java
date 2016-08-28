package joshie.harvest.animals.packet;

import joshie.harvest.api.animals.IAnimalTracked;
import joshie.harvest.core.network.Packet;
import net.minecraft.entity.player.EntityPlayer;

@Packet(Packet.Side.CLIENT)
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