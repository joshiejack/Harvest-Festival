package joshie.harvest.core.network;

import io.netty.buffer.ByteBuf;
import joshie.harvest.api.relations.IRelatable;
import joshie.harvest.api.relations.IRelatableDataHandler;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.player.relationships.RelationshipHelper;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;

public class PacketSyncRelationship implements IMessage, IMessageHandler<PacketSyncRelationship, IMessage> {
    private IRelatableDataHandler handler;
    private IRelatable relatable;
    private short value;
    private boolean particles;

    public PacketSyncRelationship() {}
    public PacketSyncRelationship(IRelatable relatable, short value, boolean particles) {
        this.relatable = relatable;
        this.value = value;
        this.particles = particles;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeShort(value);
        buf.writeBoolean(particles);
        ByteBufUtils.writeUTF8String(buf, relatable.getDataHandler().name());
        relatable.getDataHandler().toBytes(relatable, buf);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        value = buf.readShort();
        particles = buf.readBoolean();
        String handlerName = ByteBufUtils.readUTF8String(buf);
        handler = RelationshipHelper.getHandler(handlerName).copy();
        handler.fromBytes(buf);
    }

    @Override
    public IMessage onMessage(PacketSyncRelationship message, MessageContext ctx) {
        IRelatable relatable = message.handler.onMessage(message.particles);
        if (relatable != null) {
            HFTrackers.getClientPlayerTracker().getRelationships().setRelationship(message.relatable, message.value);
        }

        return null;
    }
}