package joshie.harvest.animals.packets;

import joshie.harvest.api.animals.IAnimalTracked;
import joshie.harvest.core.network.Packet;
import joshie.harvest.core.network.Packet.Side;
import joshie.harvest.core.network.PenguinPacket;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;

@Packet(Side.SERVER)
public class PacketDismount extends PenguinPacket {
    public PacketDismount() {}

    @Override
    public void handlePacket(EntityPlayer player) {
        EntityAnimal entity = (EntityAnimal) player.getRidingEntity();
        entity.startRiding(null);
        entity.rotationPitch = player.rotationPitch;
        entity.rotationYaw = player.rotationYaw;
        entity.moveRelative(0F, 1.0F, 1.25F);
        ((IAnimalTracked) entity).getData().dismount(player);
    }
}