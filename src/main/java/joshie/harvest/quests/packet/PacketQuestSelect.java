package joshie.harvest.quests.packet;

import io.netty.buffer.ByteBuf;
import joshie.harvest.HarvestFestival;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.api.quests.Selection;
import joshie.harvest.core.handlers.GuiHandler;
import joshie.harvest.core.network.Packet;
import joshie.harvest.core.network.Packet.Side;
import joshie.harvest.npcs.NPCHelper;
import joshie.harvest.npcs.entity.EntityNPC;
import joshie.harvest.quests.QuestHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.Event.Result;

@Packet(Side.SERVER)
public class PacketQuestSelect extends PacketSharedSync {
    private int quest;
    private int npcID;
    private int selected;

    @SuppressWarnings("unused")
    public PacketQuestSelect() {}
    public PacketQuestSelect(Quest quest, EntityNPC npc, int selected) {
        this.quest = Quest.REGISTRY.getValues().indexOf(quest);
        this.npcID = npc.getEntityId();
        this.selected = selected;
    }

    @Override
    public void toBytes(ByteBuf to) {
        super.toBytes(to);
        to.writeInt(quest);
        to.writeInt(npcID);
        to.writeByte(selected);
    }

    @Override
    public void fromBytes(ByteBuf from) {
        super.fromBytes(from);
        quest = from.readInt();
        npcID = from.readInt();
        selected = from.readByte();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void handlePacket(EntityPlayer player) {
        EntityNPC npc = (EntityNPC) player.worldObj.getEntityByID(npcID);
        if (npc != null) {
            BlockPos pos = new BlockPos(npc);
            if (quest == -1 && npc.getNPC().getShop(player.worldObj, pos, player) != null) {
                Selection selection = NPCHelper.getShopSelection(player.worldObj, pos, npc.getNPC(), player);
                Result result = selection.onSelected(player, npc, null, selected);
                if (result == Result.ALLOW) {
                    player.openGui(HarvestFestival.instance, GuiHandler.NPC, player.worldObj, npc.getEntityId(), -1, -1);
                } else if (result == Result.DENY) player.closeScreen();
            } else {
                Quest theQuest = QuestHelper.getSelectiomFromID(player, quest);
                Selection selection = theQuest != null ? theQuest.getSelection(player, npc) : null;
                if (selection != null) {
                    Result result = selection.onSelected(player, npc, theQuest, selected);
                    HFApi.quests.syncData(theQuest, player); //Sync to the client
                    if (result == Result.ALLOW) {
                        player.openGui(HarvestFestival.instance, GuiHandler.NPC, player.worldObj, npc.getEntityId(), -1, -1);
                    } else if (result == Result.DENY) player.closeScreen();
                }
            }
        }
    }
}
