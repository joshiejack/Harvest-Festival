package joshie.harvest.npc.gui;

import joshie.harvest.HarvestFestival;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.api.quests.QuestQuestion;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.base.gui.ContainerBase;
import joshie.harvest.npc.entity.EntityNPC;
import joshie.harvest.quests.QuestHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;

import java.util.Set;

import static joshie.harvest.core.handlers.GuiHandler.NPC;
import static joshie.harvest.core.handlers.GuiHandler.SHOP_OPTIONS;

public class ContainerNPCChat extends ContainerBase {
    protected final EntityNPC npc;
    protected final EnumHand hand;
    private int nextGui = -1;
    private boolean open = false;
    private boolean sneaking = false;

    public ContainerNPCChat(EntityPlayer player, EntityNPC npc, EnumHand hand, int nextGui) {
        this.npc = npc;
        this.hand = hand;
        this.nextGui = nextGui;
        this.open = true;
        this.sneaking = player.isSneaking();
    }

    @Override
    public void onContainerClosed(EntityPlayer player) {
        if (!player.worldObj.isRemote) {
            HFTrackers.getPlayerTrackerFromPlayer(player).getRelationships().talkTo(player, npc.getNPC().getUUID());
        }

       if (open && nextGui >= -1) {
            open = false; //To cancel out infinite loop

            Quest selection = QuestHelper.getSelection(player, npc);
            if (!HFApi.quests.getCurrentQuests(player).contains(selection)) selection = null;
            if (selection instanceof QuestQuestion && ((QuestQuestion)selection).isCompletedEarly) selection = null;
            if (selection == null && nextGui == -1) {
                npc.setTalking(null);
                Set<Quest> quests = HFApi.quests.getCurrentQuests(player);
                for (Quest quest : quests) {
                    if (quest.getNPCs().contains(npc.getNPC())) {
                        quest.onChatClosed(player, npc, npc.getNPC(), sneaking);
                    }
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
