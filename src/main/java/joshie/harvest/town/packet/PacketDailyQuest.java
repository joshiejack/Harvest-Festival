package joshie.harvest.town.packet;

import io.netty.buffer.ByteBuf;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.network.Packet;
import joshie.harvest.core.network.Packet.Side;
import joshie.harvest.core.network.PenguinPacket;
import joshie.harvest.town.TownHelper;
import joshie.harvest.town.data.TownData;
import joshie.harvest.town.tracker.TownTrackerClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.util.UUID;

@Packet(Side.CLIENT)
public class PacketDailyQuest extends PenguinPacket {
    private UUID uuid;
    private Quest quest;

    @SuppressWarnings("unused")
    public PacketDailyQuest() {}
    public PacketDailyQuest(UUID uuid, Quest quest) {
        this.uuid = uuid;
        this.quest = quest;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, uuid.toString());
        buf.writeBoolean(quest != null);
        if (quest != null) {
            ByteBufUtils.writeUTF8String(buf, quest.getRegistryName().toString());
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        uuid = UUID.fromString(ByteBufUtils.readUTF8String(buf));
        if (buf.readBoolean()) {
            quest = Quest.REGISTRY.getValue(new ResourceLocation(ByteBufUtils.readUTF8String(buf)));
        }
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        TownData data = TownHelper.getTownByID(player.worldObj, uuid);
        if (data != null) {
            HFTrackers.<TownTrackerClient>getTowns(player.worldObj).getTownByID(uuid).setDailyQuest(quest);
        }
    }
}
