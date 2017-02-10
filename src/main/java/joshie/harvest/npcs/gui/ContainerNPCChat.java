package joshie.harvest.npcs.gui;

import joshie.harvest.HarvestFestival;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.api.quests.Selection;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.base.gui.ContainerBase;
import joshie.harvest.core.handlers.GuiHandler;
import joshie.harvest.npcs.entity.EntityNPC;
import joshie.harvest.quests.QuestHelper;
import net.minecraft.entity.player.EntityPlayer;

import static joshie.harvest.core.handlers.GuiHandler.*;

public class ContainerNPCChat extends ContainerBase {
    protected final EntityNPC npc;
    protected final Quest quest;
    protected int nextGui;
    private boolean hasBeenClosed = false;
    private boolean sneaking = false;

    public ContainerNPCChat(EntityPlayer player, EntityNPC npc, int nextGui) {
        this.npc = npc;
        this.nextGui = nextGui;
        this.hasBeenClosed = false;
        this.sneaking = player.isSneaking();
        this.quest = QuestHelper.getCurrentQuest(player, npc);
        if (this.quest != null && nextGui == GuiHandler.NEXT_NONE) {
            Selection selection = this.quest.getSelection(player, npc.getNPC());
            if (selection != null && !selection.isSelected()) {
                this.nextGui = SELECTION;
            }
        }
    }

    @Override
    public void onContainerClosed(EntityPlayer player) {
        if (!hasBeenClosed) {
            hasBeenClosed = true; //Mark as having been closed, so we don't keep reopening guis

            if (nextGui == GuiHandler.NEXT_NONE) {
                npc.setTalking(null); //
                if (quest != null) quest.onChatClosed(player, npc, npc.getNPC(), sneaking);
            } else if (nextGui == SHOP_OPTIONS) {
                player.openGui(HarvestFestival.instance, SHOP_OPTIONS, player.worldObj, npc.getEntityId(), 0, NEXT_NONE);
            } else if (quest != null) {
                player.openGui(HarvestFestival.instance, SELECTION, player.worldObj, npc.getEntityId(), 0, Quest.REGISTRY.getValues().indexOf(Quest.REGISTRY.getValue(quest.getRegistryName())));
            }

            //Add the bonus RP after doing quest based stuff.
            if (!player.worldObj.isRemote) {
                HFTrackers.getPlayerTrackerFromPlayer(player).getRelationships().talkTo(player, npc.getNPC().getUUID());
            }
        }

        /*
       if (hasBeenClosed && nextGui >= -1) {
            hasBeenClosed = false; //To cancel out infinite loop

            Quest selection = QuestHelper.getSelection(player, npc);
            if (!HFApi.quests.getCurrentQuests(player).contains(selection)) selection = null;
            if (selection instanceof QuestQuestion && ((QuestQuestion)selection).isCompletedEarly) selection = null;
            if (selection == null && nextGui == -1) {
                npc.setTalking(null); //We're no longer talking
                quest.onChatClosed(player, npc, npc.getNPC(), sneaking);
            }

            if (!player.worldObj.isRemote) {
                if (selection != null && nextGui != SHOP_OPTIONS) {
                    player.openGui(HarvestFestival.instance, NPC, player.worldObj, npc.getEntityId(), 0, Quest.REGISTRY.getValues().indexOf(Quest.REGISTRY.getValue(selection.getRegistryName())));
                } else if (nextGui != -1) player.openGui(HarvestFestival.instance, nextGui, player.worldObj, npc.getEntityId(), 0, -1);
            }
        }*/
    }
}
