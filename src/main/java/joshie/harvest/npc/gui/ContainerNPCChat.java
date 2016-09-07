package joshie.harvest.npc.gui;

import joshie.harvest.HarvestFestival;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.util.ContainerBase;
import joshie.harvest.npc.entity.EntityNPC;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;

import java.util.Set;

import static joshie.harvest.api.quests.Quest.EventType.CLOSED_CHAT;
import static joshie.harvest.core.handlers.GuiHandler.NPC;
import static joshie.harvest.core.handlers.GuiHandler.SHOP_OPTIONS;

public class ContainerNPCChat extends ContainerBase {
    protected EntityNPC npc;
    protected EnumHand hand;
    private int nextGui = -1;
    private boolean open = false;

    public ContainerNPCChat(EntityNPC npc, EnumHand hand, int nextGui) {
        this.npc = npc;
        this.hand = hand;
        this.nextGui = nextGui;
        this.open = true;
    }

    @Override
    public void onContainerClosed(EntityPlayer player) {
        if (!player.worldObj.isRemote) {
            HFTrackers.getPlayerTrackerFromPlayer(player).getRelationships().talkTo(player, npc.getRelatable());
        }

       if (open && nextGui >= -1) {
            open = false; //To cancel out infinite loop

            Quest selection = HFTrackers.getPlayerTrackerFromPlayer(player).getQuests().getSelection(player, npc);
            if (selection == null && nextGui == -1) {
                npc.setTalking(null);
                Set<Quest> quests = HFApi.quests.getCurrentQuests(player, CLOSED_CHAT);
                for (Quest quest : quests) {
                    quest.onChatClosed(player, npc, npc.getNPC());
                }
            }

            if (!player.worldObj.isRemote) {
                if (selection != null && nextGui != SHOP_OPTIONS) {
                    player.openGui(HarvestFestival.instance, NPC, player.worldObj, npc.getEntityId(), 0, Quest.REGISTRY.getValues().indexOf(Quest.REGISTRY.getValue(selection.getRegistryName())));
                } else if (nextGui != -1) player.openGui(HarvestFestival.instance, nextGui, player.worldObj, npc.getEntityId(), 0, -1);
            }
        }
    }
}
