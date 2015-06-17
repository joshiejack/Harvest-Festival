package joshie.harvest.core.network;

import io.netty.buffer.ByteBuf;
import joshie.harvest.api.relations.IRelatable;
import joshie.harvest.api.relations.IRelatableDataHandler;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.player.relationships.RelationshipHelper;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketSyncMarriage implements IMessage, IMessageHandler<PacketSyncMarriage, IMessage> {
    private IRelatable relatable;
    private boolean divorce;
    private IRelatableDataHandler handler;

    public PacketSyncMarriage() {}
    public PacketSyncMarriage(IRelatable relatable, boolean divorce) {
        this.relatable = relatable;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, relatable.getDataHandler().name());
        relatable.getDataHandler().toBytes(relatable, buf);
        buf.writeBoolean(divorce);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        String handlerName = ByteBufUtils.readUTF8String(buf);
        handler = RelationshipHelper.getHandler(handlerName).copy();
        divorce = buf.readBoolean();
    }

    @Override
    public IMessage onMessage(PacketSyncMarriage message, MessageContext ctx) {
        IRelatable relatable = message.handler.onMessage(false);
        if (relatable != null) {
            HFTrackers.getClientPlayerTracker().getRelationships().setMarriageState(relatable, message.divorce);
        }

        return null;
    }
}