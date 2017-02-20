package joshie.harvest.player.packet;

import io.netty.buffer.ByteBuf;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.helpers.NBTHelper;
import joshie.harvest.core.network.Packet;
import joshie.harvest.core.network.PenguinPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.util.Set;

@Packet(Packet.Side.CLIENT)
public class PacketSyncUnread extends PenguinPacket {
    private Set<ResourceLocation> set;

    public PacketSyncUnread() { }
    public PacketSyncUnread(Set<ResourceLocation> set) {
        this.set = set;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setTag("Unread", NBTHelper.writeResourceSet(set));
        ByteBufUtils.writeTag(buf, tag);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        set = NBTHelper.readResourceSet(ByteBufUtils.readTag(buf), "Unread");
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        HFTrackers.getClientPlayerTracker().getTracking().setUnread(set);
    }
}