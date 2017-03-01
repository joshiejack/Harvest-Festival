package joshie.harvest.player.packet;

import io.netty.buffer.ByteBuf;
import joshie.harvest.HarvestFestival;
import joshie.harvest.api.npc.NPC;
import joshie.harvest.core.network.PenguinPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import org.apache.logging.log4j.Level;

public abstract class PacketRelationship extends PenguinPacket {
    private NPC npc;

    public PacketRelationship() {}
    public PacketRelationship(NPC npc) {
        this.npc = npc;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, npc.getResource().toString());
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        try {
            npc = NPC.REGISTRY.get(new ResourceLocation(ByteBufUtils.readUTF8String(buf)));
        } catch (Exception e) { HarvestFestival.LOGGER.log(Level.ERROR, "Failed to read a sync gift packet correctly"); }
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        handleRelationship(player, npc);
    }

    protected abstract void handleRelationship(EntityPlayer player, NPC npc);
}