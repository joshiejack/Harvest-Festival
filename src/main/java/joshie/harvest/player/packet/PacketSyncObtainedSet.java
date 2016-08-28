package joshie.harvest.player.packet;

import io.netty.buffer.ByteBuf;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.helpers.NBTHelper;
import joshie.harvest.core.network.Packet;
import joshie.harvest.core.network.PenguinPacket;
import joshie.harvest.core.util.holder.ItemStackHolder;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.util.Set;

@Packet(Packet.Side.CLIENT)
public class PacketSyncObtainedSet extends PenguinPacket {
    private Set<ItemStackHolder> set;

    public PacketSyncObtainedSet() { }
    public PacketSyncObtainedSet(Set<ItemStackHolder> set) {
        this.set = set;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setTag("Obtained", NBTHelper.writeCollection(set));
        ByteBufUtils.writeTag(buf, tag);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        NBTTagCompound tag = ByteBufUtils.readTag(buf);
        set = NBTHelper.readHashSet(ItemStackHolder.class, tag.getTagList("Obtained", 10));
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        HFTrackers.getClientPlayerTracker().getTracking().setObtained(set);
    }
}