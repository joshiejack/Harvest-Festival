package joshie.harvest.core.network;

import io.netty.buffer.ByteBuf;
import joshie.harvest.api.relations.IDataHandler;
import joshie.harvest.api.relations.IRelatable;
import joshie.harvest.core.handlers.DataHelper;
import joshie.harvest.relations.RelationshipHelper;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketSyncRelationship implements IMessage, IMessageHandler<PacketSyncRelationship, IMessage> {
    private IRelatable relatable;
    private short value;
    private Object[] data;
    private IDataHandler handler;

    public PacketSyncRelationship() {}
    public PacketSyncRelationship(IRelatable relatable, short value, Object... data) {
        this.relatable = relatable;
        this.value = value;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeShort(value);
        ByteBufUtils.writeUTF8String(buf, relatable.getDataHandler().name());
        relatable.getDataHandler().toBytes(relatable, buf, data);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        value = buf.readShort();
        String handlerName = ByteBufUtils.readUTF8String(buf);
        handler = RelationshipHelper.getHandler(handlerName).copy();
    }

    @Override
    public IMessage onMessage(PacketSyncRelationship message, MessageContext ctx) {
        IRelatable relatable = message.handler.onMessage();
        if (relatable != null) {
            DataHelper.getPlayerTracker(null).getRelationships().setRelationship(relatable, message.value);
        }

        return null;
    }
}