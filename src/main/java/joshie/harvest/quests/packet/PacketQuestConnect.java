package joshie.harvest.quests.packet;

import io.netty.buffer.ByteBuf;
import joshie.harvest.core.network.Packet;
import joshie.harvest.core.network.Packet.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;

@Packet(Side.CLIENT)
public class PacketQuestConnect extends PacketQuest {
    private NBTTagCompound tag;

    @SuppressWarnings("unused")
    public PacketQuestConnect() {}
    public PacketQuestConnect(NBTTagCompound tag) {
        this.tag = tag;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        super.toBytes(buf);
        ByteBufUtils.writeTag(buf, tag);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        super.fromBytes(buf);
        tag = ByteBufUtils.readTag(buf);
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        getQuestDataFromPlayer(player).readFromNBT(tag);
    }
}