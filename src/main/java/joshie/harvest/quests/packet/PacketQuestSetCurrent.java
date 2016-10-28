package joshie.harvest.quests.packet;

import io.netty.buffer.ByteBuf;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.core.network.Packet;
import joshie.harvest.quests.data.QuestDataClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.ByteBufUtils;

@Packet(Packet.Side.CLIENT)
public class PacketQuestSetCurrent extends PacketQuest<QuestDataClient> {
    private Quest quest;

    public PacketQuestSetCurrent() {}
    public PacketQuestSetCurrent(Quest quest) {
        this.quest = quest;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        super.toBytes(buf);
        buf.writeBoolean(quest == null);
        if (quest != null) {
            ByteBufUtils.writeUTF8String(buf, quest.getRegistryName().toString());
            ByteBufUtils.writeTag(buf, quest.writeToNBT(new NBTTagCompound()));
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        super.fromBytes(buf);
        boolean isNull = buf.readBoolean();
        if (!isNull) {
            Quest q = Quest.REGISTRY.getValue(new ResourceLocation(ByteBufUtils.readUTF8String(buf)));
            try {
                quest = q.getClass().newInstance().setRegistryName(q.getRegistryName());
                quest.readFromNBT(ByteBufUtils.readTag(buf));
            } catch (Exception e) {}
        }
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        getQuestDataFromPlayer(player).addAsCurrent(quest);
    }
}