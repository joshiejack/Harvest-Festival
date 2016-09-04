package joshie.harvest.quests.packet;

import io.netty.buffer.ByteBuf;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.network.Packet;
import joshie.harvest.core.network.PenguinPacket;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.ByteBufUtils;

@Packet
public class PacketRequestEntity extends PenguinPacket {
    private Quest quest;
    private String entity;

    public PacketRequestEntity() {}
    public PacketRequestEntity(Quest quest, String entity) {
        this.quest = quest;
        this.entity = entity;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, entity);
        ByteBufUtils.writeUTF8String(buf, quest.getRegistryName().toString());
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        quest = Quest.REGISTRY.getValue(new ResourceLocation(ByteBufUtils.readUTF8String(buf)));
        entity = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        Quest real = HFTrackers.getPlayerTrackerFromPlayer(player).getQuests().getAQuest(quest);
        if (real.canSpawnEntity(entity)) {
            Entity theEntity = EntityList.createEntityByIDFromName(entity, player.worldObj);
            if (theEntity != null) {
                theEntity.setPosition(player.posX, player.posY, player.posZ);
                player.worldObj.spawnEntityInWorld(theEntity);
            }
        }
    }
}