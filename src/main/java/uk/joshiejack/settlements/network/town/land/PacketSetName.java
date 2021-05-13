package uk.joshiejack.settlements.network.town.land;

import io.netty.buffer.ByteBuf;
import uk.joshiejack.settlements.AdventureDataLoader;
import uk.joshiejack.settlements.network.town.PacketAbstractTownSync;
import uk.joshiejack.settlements.world.town.Town;
import uk.joshiejack.penguinlib.client.PenguinTeamsClient;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import uk.joshiejack.penguinlib.util.helpers.minecraft.PlayerHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.util.UUID;

@PenguinLoader
public class PacketSetName extends PacketAbstractTownSync {
    private String name;

    public PacketSetName() {}
    public PacketSetName(int dimension, int id, String name) {
        super(dimension, id);
        this.name = name;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        super.toBytes(buf);
        ByteBufUtils.writeUTF8String(buf, name);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        super.fromBytes(buf);
        name = ByteBufUtils.readUTF8String(buf);
    }


    @Override
    protected void handlePacket(EntityPlayer player, Town<?> town) {
        if (!player.world.isRemote) {
            UUID playerUUID = PlayerHelper.getUUIDForPlayer(player);
            if (PenguinTeamsClient.getInstance().members().contains(playerUUID) &&
                    playerUUID.equals(PenguinTeamsClient.getInstance().getOwner())) { //If we're the owner
                town.getCharter().setName(name);
                AdventureDataLoader.get(player.world).markDirty();
                PenguinNetwork.sendToEveryone(new PacketSetName(dimension, id, name));
            }
        } else town.getCharter().setName(name);
    }
}
