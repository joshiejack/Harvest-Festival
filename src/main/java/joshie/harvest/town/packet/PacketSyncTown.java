package joshie.harvest.town.packet;

import io.netty.buffer.ByteBuf;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.network.PenguinPacket;
import joshie.harvest.town.data.TownDataClient;
import joshie.harvest.town.tracker.TownTrackerClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.util.UUID;


public abstract class PacketSyncTown extends PenguinPacket {
    private UUID town;

    @SuppressWarnings("unused")
    public PacketSyncTown(){}
    public PacketSyncTown(UUID town) {
        this.town = town;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, town.toString());
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        town = UUID.fromString(ByteBufUtils.readUTF8String(buf));
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        TownDataClient townData = HFTrackers.<TownTrackerClient>getTowns(player.worldObj).getTownByID(town);
        if (townData != null) {
            handlePacket(townData);
        }
    }

    public abstract void handlePacket(TownDataClient townData);
}
