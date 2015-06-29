package joshie.harvest.core.network.animals;

import io.netty.buffer.ByteBuf;

public abstract class AbstractSyncBoolean extends AbstractSyncAnimal {
    protected boolean data;

    public AbstractSyncBoolean() {}

    public AbstractSyncBoolean(int id, boolean data) {
        super(id);
        this.data = data;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        super.toBytes(buf);
        buf.writeBoolean(data);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        super.fromBytes(buf);
        data = buf.readBoolean();
    }
    
    public boolean getBoolean(AbstractSyncBoolean message) {
        return message.data;
    }
}