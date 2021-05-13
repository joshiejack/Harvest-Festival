package uk.joshiejack.penguinlib.events;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import uk.joshiejack.penguinlib.network.packet.PacketOpenUniversalGuide;

import javax.annotation.Nullable;


public class UniversalGuidePacketEvent extends PlayerEvent {
    private PacketOpenUniversalGuide packet;

    public UniversalGuidePacketEvent(EntityPlayer player) {
        super(player);
    }

    public void setPacket(PacketOpenUniversalGuide packet) {
        this.packet = packet;
    }

    @Nullable
    public PacketOpenUniversalGuide getPacket() {
        return packet;
    }
}
