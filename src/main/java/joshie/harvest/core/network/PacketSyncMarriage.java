package joshie.harvest.core.network;

import io.netty.buffer.ByteBuf;
import joshie.harvest.api.relations.IRelatable;
import joshie.harvest.api.relations.IRelatableDataHandler;
import joshie.harvest.core.handlers.DataHelper;
import joshie.harvest.relations.RelationshipHelper;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketSyncMarriage implements IMessage, IMessageHandler<PacketSyncMarriage, IMessage> {
    private IRelatable relatable;
    private IRelatableDataHandler handler;

    public PacketSyncMarriage() {}
    public PacketSyncMarriage(IRelatable relatable) {
        this.relatable = relatable;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, relatable.getDataHandler().name());
        relatable.getDataHandler().toBytes(relatable, buf, false);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        String handlerName = ByteBufUtils.readUTF8String(buf);
        handler = RelationshipHelper.getHandler(handlerName).copy();
    }

    @Override
    public IMessage onMessage(PacketSyncMarriage message, MessageContext ctx) {
        IRelatable relatable = message.handler.onMessage();
        if (relatable != null) {
            DataHelper.getPlayerTracker(null).getRelationships().setMarried(relatable);
        }

        return null;
    }
}