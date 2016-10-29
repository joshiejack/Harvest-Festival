package joshie.harvest.quests.data;

import joshie.harvest.api.npc.INPC;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.util.interfaces.IQuestMaster;
import joshie.harvest.quests.packet.PacketQuestCompleted;
import joshie.harvest.quests.packet.PacketQuestSetCurrent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;

import java.util.HashSet;
import java.util.Set;

public class QuestDataServer extends QuestData {
    private final HashSet<Quest> finished = new HashSet<>();
    private final IQuestMaster master;

    public QuestDataServer(IQuestMaster master) {
        this.master = master;
    }

    @Override
    public boolean startQuest(Quest q) {
        try {
            if (!finished.contains(q) || q.isRepeatable()) {
                Quest quest = q.getClass().newInstance().setRegistryName(q.getRegistryName()).setStage(0); //Set the current quest to your new
                current.add(quest);
                master.sync(null, new PacketQuestSetCurrent(quest));
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
            localQuest.onQuestCompleted(player);
        } else {
            finished.add(quest);
            quest.onQuestCompleted(player);
        }

        HFTrackers.markDirty(player.worldObj);
        //Sync everything
        master.sync(player, new PacketQuestCompleted(quest, true)); //Let this player claim the reward
        master.sync(null, new PacketQuestCompleted(quest, false)); //Let the server know this was completed
        syncAllQuests(); //Update the world on these quests, everytime one is completed
    }
    
    public void syncAllQuests() {
        for (Quest quest : Quest.REGISTRY.getValues()) {
            syncQuest(quest);
        }
    }

    private void syncQuest(Quest quest) {
        if (quest.getQuestType() != master.getQuestType()) return; //If we aren't the same quest type, we don't get counted
        //Check if the quest can be complete
        //If the quest isn't finished, do stuff
        if (!finished.contains(quest) || quest.isRepeatable()) {
            //If the quest is in the currently active list, mark it as current
            if (current.contains(quest)) {
                //Send a packet, fetching the actual quest details that are saved, so we're update to date on the info
                master.sync(null, new PacketQuestSetCurrent(getAQuest(quest)));
            } else {
                //Now the quests aren't in the current list has been determined, let's determine whether this quest is valid for being collected
                //If the quest can be started, we should enable the quest
                if (canStart(quest, current, finished)) {
                    startQuest(quest); //master.sync(new PacketQuestSetAvailable(quest));
                }
            }
        }
    }

    private boolean canStart(Quest quest, HashSet<Quest> active, HashSet<Quest> finished) {
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

    public void readFromNBT(NBTTagCompound nbt) {
        if (nbt.hasKey("CurrentQuests")) {
            NBTTagList list = nbt.getTagList("CurrentQuests", 10);
            for (int i = 0; i < list.tagCount(); i++) {
                NBTTagCompound tag = list.getCompoundTagAt(i);
                Quest q = Quest.REGISTRY.getValue(new ResourceLocation(tag.getString("QuestID")));
                try {
                    Quest quest = q.getClass().newInstance().setRegistryName(q.getRegistryName());
                    quest.readFromNBT(tag);
                    current.add(quest);
                } catch (InstantiationException | IllegalAccessException e){ /**/}
            }
        }

        if (nbt.hasKey("FinishedQuests")) {
            NBTTagList list = nbt.getTagList("FinishedQuests", 8);
            for (int i = 0; i < list.tagCount(); i++) {
                finished.add(Quest.REGISTRY.getValue(new ResourceLocation((list.getStringTagAt(i)))));
            }
        }
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        NBTTagList quests = new NBTTagList();
        for (Quest s : current) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("QuestID", s.getRegistryName().toString());
            s.writeToNBT(tag);
            quests.appendTag(tag);
        }

        nbt.setTag("CurrentQuests", quests);

        NBTTagList done = new NBTTagList();
        for (Quest s : finished) {
            if (s != null) {
                done.appendTag(new NBTTagString(s.getRegistryName().toString()));
            }
        }

        nbt.setTag("FinishedQuests", done);
        return nbt;
    }
}
