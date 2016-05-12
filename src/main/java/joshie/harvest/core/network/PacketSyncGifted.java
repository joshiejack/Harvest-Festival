package joshie.harvest.core.network;

import io.netty.buffer.ByteBuf;
import joshie.harvest.api.relations.IRelatable;
import joshie.harvest.api.relations.IRelatableDataHandler;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.network.penguin.PenguinPacket;
import joshie.harvest.player.relationships.RelationshipHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class PacketSyncGifted extends PenguinPacket {
    private IRelatable relatable;
    private IRelatableDataHandler handler;
    private boolean gifted;

    public PacketSyncGifted() {}

    public PacketSyncGifted(IRelatable relatable, boolean gifted) {
        this.relatable = relatable;
        this.gifted = gifted;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, relatable.getDataHandler().name());
        relatable.getDataHandler().toBytes(relatable, buf);
        buf.writeBoolean(gifted);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        String handlerName = ByteBufUtils.readUTF8String(buf);
        handler = RelationshipHelper.getHandler(handlerName).copy();
        handler.fromBytes(buf);
        gifted = buf.readBoolean();
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        IRelatable relatable = handler.onMessage(false);
        if (relatable != null) {
            HFTrackers.getClientPlayerTracker().getRelationships().setGifted(relatable, gifted);
        }
    }
}