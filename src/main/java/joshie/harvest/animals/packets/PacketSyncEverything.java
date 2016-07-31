package joshie.harvest.animals.packets;

import io.netty.buffer.ByteBuf;
import joshie.harvest.api.animals.IAnimalData;
import joshie.harvest.core.network.Packet;
import net.minecraft.entity.player.EntityPlayer;

@Packet(Packet.Side.CLIENT)
public class PacketSyncEverything extends AbstractSyncAnimal {
    protected IAnimalData data;
    protected ByteBuf buf;

    public PacketSyncEverything() {}
    public PacketSyncEverything(int id, IAnimalData data) {
        super(id);
        this.data = data;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        super.toBytes(buf);
        data.toBytes(buf);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        super.fromBytes(buf);
        this.buf = buf;
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        if (getAnimal() != null) {
            (getAnimal()).getData().fromBytes(buf);
        }
    }
}