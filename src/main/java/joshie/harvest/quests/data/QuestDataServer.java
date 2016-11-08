package joshie.harvest.quests.data;

import joshie.harvest.api.npc.INPC;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.api.quests.QuestType;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.util.interfaces.IQuestMaster;
import joshie.harvest.quests.packet.PacketQuestCompleted;
import joshie.harvest.quests.packet.PacketQuestConnect;
import joshie.harvest.quests.packet.PacketQuestSetCurrent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;
import java.util.Set;

public class QuestDataServer extends QuestData {
    private final IQuestMaster master;

    public QuestDataServer(IQuestMaster master) {
        this.master = master;
    }

    @Override
    public boolean startQuest(Quest q, boolean sync) {
        try {
            if (!finished.contains(q) || q.isRepeatable()) {
                Quest quest = q.getClass().newInstance().setRegistryName(q.getRegistryName()).setStage(0); //Set the current quest to your new
                current.add(quest);
                if (sync ) {
                    master.sync(null, new PacketQuestSetCurrent(quest));
                }

                return true;
            } else return false;
        } catch (Exception ignored) { return false; }
    }

    //Quests should always REMOVE from the current quests, and add to the finished quests THEMSELVES
    //Only the person who actually completed the quest, will get the reward
    @Override
    public void markCompleted(EntityPlayer player, Quest quest, boolean rewards) {
        Quest localQuest = getAQuest(quest);
        if (localQuest != null) {
            finished.add(localQuest);
            current.remove(localQuest);
            if (rewards) localQuest.onQuestCompleted(player);
        } else {
            finished.add(quest);
            if (rewards) quest.onQuestCompleted(player);
        }

        HFTrackers.markDirty(player.worldObj);
        //Sync everything
        if ((quest.getQuestType() == QuestType.PLAYER || quest.getQuestType() == QuestType.TOWN && rewards)) master.sync(player, new PacketQuestCompleted(quest, rewards)); //Let this player claim the reward
        if (quest.getQuestType() == QuestType.TOWN) master.sync(null, new PacketQuestCompleted(quest, false)); //Let the rest of the server know this was completed
        updateQuests(true); //Update the world on these quests, everytime one is completed
    }
    
    public void updateQuests(boolean sync) {
        for (Quest quest : Quest.REGISTRY.getValues()) {
            updateQuests(quest, sync);
        }
    }

    public void sync(@Nullable EntityPlayerMP player) {
        updateQuests(false); //Don't bother syncing as we're going to send it all at once!
        master.sync(player, new PacketQuestConnect(writeToNBT(new NBTTagCompound())));
    }

    private void updateQuests(Quest quest, boolean sync) {
        if (quest.getQuestType() != master.getQuestType()) return; //If we aren't the same quest type, we don't get counted
        //Check if the quest can be complete
        //If the quest isn't finished, do stuff
        if (!finished.contains(quest) || quest.isRepeatable()) {
            //If we aren't already working on this quest, and we can start it, then start the quest
            if (!current.contains(quest) && canStart(quest, current, finished)) {
                startQuest(quest, sync);
            }
        }
    }

    private boolean canStart(Quest quest, Set<Quest> active, Set<Quest> finished) {
        //Loops through all the active quests, if any of the quests are real and contain npcs that are used by this quest, we can not start it
        Set<INPC> npcs = quest.getNPCs();
        if (npcs != null) {
            for (Quest a : active) {
                if (a.isRealQuest()) {
                    for (INPC npc : npcs) {
                        for (INPC n : a.getNPCs()) {
                            if (n.equals(npc)) {
                                return false;
                            }
                        }
                    }
                }
            }
        }

        return quest.canStartQuest(active, finished);
    }
}
