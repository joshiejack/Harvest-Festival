package joshie.harvest.town.packet;

import io.netty.buffer.ByteBuf;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.network.Packet;
import joshie.harvest.core.network.Packet.Side;
import joshie.harvest.core.network.PenguinPacket;
import joshie.harvest.town.data.TownData;
import joshie.harvest.town.data.TownDataClient;
import joshie.harvest.town.tracker.TownTrackerClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;

@Packet(Side.CLIENT)
public class PacketNewTown extends PenguinPacket {
    public PacketNewTown() {}
    private TownData data;

    public PacketNewTown(TownData data) {
        this.data = data;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        NBTTagCompound tag = new NBTTagCompound();
        data.writeToNBT(tag);
        ByteBufUtils.writeTag(buf, tag);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        data = new TownDataClient();
        data.readFromNBT(ByteBufUtils.readTag(buf));
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        HFTrackers.<TownTrackerClient>getTownTracker(player.worldObj).addTown((TownDataClient)data);
    }
}
