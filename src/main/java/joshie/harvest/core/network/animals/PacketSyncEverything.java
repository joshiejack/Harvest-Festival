package joshie.harvest.core.network.animals;

import io.netty.buffer.ByteBuf;
import joshie.harvest.api.animals.IAnimalData;
import net.minecraft.entity.player.EntityPlayer;

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
        (getAnimal()).getData().fromBytes(buf);
    }
}