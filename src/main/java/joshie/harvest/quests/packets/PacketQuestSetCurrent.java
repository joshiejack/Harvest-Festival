package joshie.harvest.quests.packets;

import io.netty.buffer.ByteBuf;
import joshie.harvest.core.helpers.generic.MCClientHelper;
import joshie.harvest.core.network.penguin.PenguinPacket;
import joshie.harvest.quests.Quest;
import joshie.harvest.quests.QuestHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class PacketQuestSetCurrent extends PenguinPacket {
    private Quest quest;

    public PacketQuestSetCurrent() {}
    public PacketQuestSetCurrent(Quest quest) {
        this.quest = quest;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(quest == null);
        if (quest != null) {
            ByteBufUtils.writeUTF8String(buf, quest.getRegistryName().toString());
            ByteBufUtils.writeTag(buf, quest.writeToNBT(new NBTTagCompound()));
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        boolean isNull = buf.readBoolean();
        if (!isNull) {
            Quest q = Quest.REGISTRY.getObject(new ResourceLocation(ByteBufUtils.readUTF8String(buf)));

            try {
                quest = q.getClass().newInstance().setRegistryName(q.getRegistryName());
                quest.readFromNBT(ByteBufUtils.readTag(buf));
            } catch (Exception e) {}
        }
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        QuestHelper.markAsCurrent(MCClientHelper.getPlayer(), quest);
    }
}