package uk.joshiejack.settlements.network.block;

import io.netty.buffer.ByteBuf;
import uk.joshiejack.settlements.Settlements;
import uk.joshiejack.settlements.AdventureDataLoader;
import uk.joshiejack.settlements.quest.Quest;
import uk.joshiejack.penguinlib.network.PenguinPacket;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;

@PenguinLoader(side = Side.SERVER)
public class PacketStartScript extends PenguinPacket {
    private ResourceLocation quest;

    public PacketStartScript() {}
    public PacketStartScript(ResourceLocation quest) {
        this.quest = quest;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, quest.toString());
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        quest = new ResourceLocation(ByteBufUtils.readUTF8String(buf));
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        Quest theQuest = Quest.REGISTRY.get(quest);
        theQuest.fire("canStart", player, AdventureDataLoader.get(player.world).getTrackerForQuest(player, theQuest));
        //Reopen the quest board
        player.openGui(Settlements.instance, Settlements.QUEST_BOARD, player.world, (int) player.posX, (int) player.posY, (int) player.posZ);
        //AdventureDataLoader.get(player.world).getTrackerForQuest(player, Quest.REGISTRY.get(quest)).fire("canStart", player);
    }
}
