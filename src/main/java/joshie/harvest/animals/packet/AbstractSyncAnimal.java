package joshie.harvest.animals.packet;

import io.netty.buffer.ByteBuf;
import joshie.harvest.core.network.PenguinPacket;

public abstract class AbstractSyncAnimal extends PenguinPacket {
    protected int id;

    public AbstractSyncAnimal() {}
    public AbstractSyncAnimal(int id) {
        this.id = id;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(id);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        id = buf.readInt();
    }
}