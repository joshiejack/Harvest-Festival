package joshie.harvest.quests.packets;

import io.netty.buffer.ByteBuf;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.helpers.generic.ItemHelper;
import joshie.harvest.core.network.Packet;
import joshie.harvest.core.network.PenguinPacket;
import joshie.harvest.quests.QuestHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.ByteBufUtils;

@Packet
public class PacketRequestItem extends PenguinPacket {
    private Quest quest;
    private ItemStack stack;

    public PacketRequestItem() {}
    public PacketRequestItem(Quest quest, ItemStack stack) {
        this.quest = quest;
        this.stack = stack;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeItemStack(buf, stack);
        ByteBufUtils.writeUTF8String(buf, quest.getRegistryName().toString());
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        quest = Quest.REGISTRY.getObject(new ResourceLocation(ByteBufUtils.readUTF8String(buf)));
        stack = ByteBufUtils.readItemStack(buf);
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        Quest real = HFTrackers.getPlayerTracker(player).getQuests().getAQuest(quest);
        if (real.canReward(stack)) {
            ItemHelper.addToPlayerInventory(player, stack);
        }
    }
}