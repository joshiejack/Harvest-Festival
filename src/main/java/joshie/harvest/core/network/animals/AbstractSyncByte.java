package joshie.harvest.core.network.animals;

import io.netty.buffer.ByteBuf;

public abstract class AbstractSyncByte extends AbstractSyncAnimal {
    protected byte data;

    public AbstractSyncByte() {}

    public AbstractSyncByte(int id, byte data) {
        super(id);
        this.data = data;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        super.toBytes(buf);
        buf.writeByte(data);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        super.fromBytes(buf);
        data = buf.readByte();
    }
}