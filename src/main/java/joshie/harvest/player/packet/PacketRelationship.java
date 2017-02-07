package joshie.harvest.player.packet;

import io.netty.buffer.ByteBuf;
import joshie.harvest.HarvestFestival;
import joshie.harvest.core.network.PenguinPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import org.apache.logging.log4j.Level;

import java.util.UUID;

public abstract class PacketRelationship extends PenguinPacket {
    private UUID key;

    public PacketRelationship() {}
    public PacketRelationship(UUID key) {
        this.key = key;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, key.toString());
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        try {
            key = UUID.fromString(ByteBufUtils.readUTF8String(buf));
        } catch (Exception e) { HarvestFestival.LOGGER.log(Level.ERROR, "Failed to read a sync gift packet correctly"); }
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        handleRelationship(player, key);
    }

    protected abstract void handleRelationship(EntityPlayer player, UUID key);
}