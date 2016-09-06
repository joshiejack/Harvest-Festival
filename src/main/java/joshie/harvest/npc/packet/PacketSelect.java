package joshie.harvest.npc.packet;

import io.netty.buffer.ByteBuf;
import joshie.harvest.HarvestFestival;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.api.quests.Quest.Selection;
import joshie.harvest.core.handlers.GuiHandler;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.network.Packet;
import joshie.harvest.core.network.Packet.Side;
import joshie.harvest.core.network.PenguinPacket;
import joshie.harvest.npc.entity.EntityNPC;
import joshie.harvest.npc.gui.ShopSelection;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.Event.Result;

@Packet(Side.SERVER)
public class PacketSelect extends PenguinPacket {
    private static final Selection SHOPS = new ShopSelection();
    private int quest;
    private int npcID;
    private int selected;

    public PacketSelect() {}
    public PacketSelect(Quest quest, EntityNPC npc, int selected) {
        this.quest = Quest.REGISTRY.getValues().indexOf(quest);
        this.npcID = npc.getEntityId();
        this.selected = selected;
    }

    @Override
    public void toBytes(ByteBuf to) {
        to.writeInt(quest);
        to.writeInt(npcID);
        to.writeByte(selected);
    }

    @Override
    public void fromBytes(ByteBuf from) {
        quest = from.readInt();
        npcID = from.readInt();
        selected = from.readByte();
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        Quest theQuest = null;
        Selection selection;
        EntityNPC npc = (EntityNPC) player.worldObj.getEntityByID(npcID);
        if (quest == -1) selection = SHOPS;
        else {
            theQuest = HFTrackers.getPlayerTrackerFromPlayer(player).getQuests().getAQuest(Quest.REGISTRY.getValues().get(quest));
            selection = theQuest != null ? theQuest.getSelection(player, npc.getNPC()): null;
        }

        //Check
        if (selection != null && npc != null) {
            Result result = selection.onSelected(player, npc, npc.getNPC(), theQuest, selected);
            if (result == Result.ALLOW) player.openGui(HarvestFestival.instance, GuiHandler.NPC, player.worldObj, npc.getEntityId(), -1, -1);
            else if (result == Result.DENY) player.closeScreen();
        }
    }
}
