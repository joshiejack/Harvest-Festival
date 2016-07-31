package joshie.harvest.player.packets;

import io.netty.buffer.ByteBuf;
import joshie.harvest.HarvestFestival;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.relations.IRelatable;
import joshie.harvest.api.relations.IRelatableDataHandler;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.network.Packet;
import joshie.harvest.core.network.PenguinPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import org.apache.logging.log4j.Level;

@Packet(Packet.Side.CLIENT)
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
        try {
            String handlerName = ByteBufUtils.readUTF8String(buf);
            handler = HFApi.player.getRelationshipHelper().getDataHandler(handlerName).getClass().newInstance();
            relatable = handler.fromBytes(buf);
            gifted = buf.readBoolean();
        } catch (Exception e) { HarvestFestival.LOGGER.log(Level.ERROR, "Failed to read a sync gift packet correctly"); }
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        if (relatable != null) {
            handler.onMessage(relatable, false);
            HFTrackers.getClientPlayerTracker().getRelationships().setGifted(relatable, gifted);
        }
    }
}