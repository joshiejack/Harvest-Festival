package joshie.harvest.core.network.animals;

import io.netty.buffer.ByteBuf;
import joshie.harvest.api.animals.IAnimalData;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketSyncEverything extends AbstractSyncAnimal implements IMessageHandler<PacketSyncEverything, IMessage> {
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
    public IMessage onMessage(PacketSyncEverything message, MessageContext ctx) {
        (getAnimal(message, ctx)).getData().fromBytes(message.buf);
        return null;
    }
}