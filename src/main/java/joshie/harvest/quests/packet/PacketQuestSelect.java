package joshie.harvest.quests.packet;

import io.netty.buffer.ByteBuf;
import joshie.harvest.HarvestFestival;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.api.quests.Selection;
import joshie.harvest.core.handlers.GuiHandler;
import joshie.harvest.core.network.Packet;
import joshie.harvest.core.network.Packet.Side;
import joshie.harvest.npc.entity.EntityNPC;
import joshie.harvest.npc.gui.ShopSelection;
import joshie.harvest.quests.QuestHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.Event.Result;

@Packet(Side.SERVER)
public class PacketQuestSelect extends PacketQuest {
    private static final Selection SHOPS = new ShopSelection();
    private int quest;
    private int npcID;
    private int selected;

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
        Quest theQuest = null;
        Selection selection;
        EntityNPC npc = (EntityNPC) player.worldObj.getEntityByID(npcID);
        if (npc != null) {
            if (quest == -1) selection = SHOPS;
            else {
                theQuest = QuestHelper.getSelectiomFromID(player, quest);
                selection = theQuest != null ? theQuest.getSelection(player, npc.getNPC()) : null;
            }

            //Check
            if (selection != null) {
                Result result = selection.onSelected(player, npc, npc.getNPC(), theQuest, selected);
                if (result == Result.ALLOW)
                    player.openGui(HarvestFestival.instance, GuiHandler.NPC, player.worldObj, npc.getEntityId(), -1, -1);
                else if (result == Result.DENY) player.closeScreen();
            }
        }
    }
}
