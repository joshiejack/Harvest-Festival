package joshie.harvest.player.quests;

import joshie.harvest.api.npc.INPC;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.api.quests.Quest.EventsHandled;
import joshie.harvest.player.PlayerTrackerServer;
import joshie.harvest.quests.packet.PacketQuestSetAvailable;
import joshie.harvest.quests.packet.PacketQuestSetCurrent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;

import java.util.HashSet;

import static joshie.harvest.core.network.PacketHandler.sendToClient;

public class QuestDataServer extends QuestData {
    private HashSet<Quest> finished = new HashSet<>();

    public PlayerTrackerServer master;
    public QuestDataServer(PlayerTrackerServer master) {
        this.master = master;
    }

    //Called to start a quest, is called clientside, by the startquest packet
    @Override
    public boolean startQuest(Quest q) {
        if (current.size() < 10 || !q.isRealQuest()) {
            try {
                Quest quest = q.getClass().newInstance().setRegistryName(q.getRegistryName()).setStage(0); //Set the current quest to your new
                current.add(quest);
                for (EventsHandled handled: quest.getHandledEvents()) {
                    eventHandlers.get(handled).add(quest);
                }

                syncQuest(q, master.getAndCreatePlayer());
            } catch (Exception ignored) {}
            return true;
        } else return false;
    }

    @Override
    public void setStage(Quest quest, int stage) {
        Quest q = getAQuest(quest);
        if (q != null) {
            q.setStage(stage);
        }
    }
    
    @Override
    public void markCompleted(Quest quest, boolean sendPacket) {
        markCompleted(quest);
    }

    //Quests should always REMOVE from the current quests, and add to the finished quests THEMSELVES
    public void markCompleted(Quest quest) {
        Quest q = getAQuest(quest);
        if (q != null) {
            q.claim(master.getAndCreatePlayer());
            finished.add(q);
            current.remove(q);
            for (EventsHandled handled: q.getHandledEvents()) {
                eventHandlers.get(handled).remove(q);
            }

            sync(master.getAndCreatePlayer());
        }
    }
    
    public void sync(EntityPlayerMP player) {
        for (Quest quest : Quest.REGISTRY.getValues()) {
            syncQuest(quest, player);
        }
    }

    public void syncQuest(Quest quest, EntityPlayerMP player) {
        //Check if the quest can be complete
        //If the quest isn't finished, do stuff
        if (!finished.contains(quest) || quest.isRepeatable()) {
            //If the quest is in the currently active list, mark it as current
            if (current.contains(quest)) {
                //Send a packet, fetching the actual quest details that are saved, so we're update to date on the info
                sendToClient(new PacketQuestSetCurrent(getAQuest(quest)), player);
            } else {
                //Now the quests aren't in the current list has been determined, let's determine whether this quest is valid for being collected
                //If the quest can be started, we should send it to client to be added to the available list
                if (canStart(quest, master.getAndCreatePlayer(), current, finished)) {
                    sendToClient(new PacketQuestSetAvailable(quest), player);
                }
            }
        }
    }

    private boolean canStart(Quest quest, EntityPlayer player, HashSet<Quest> active, HashSet<Quest> finished) {
        if (!quest.isRepeatable() && finished.contains(quest)) {
            return false;
        }

        //Loops through all the active quests, if any of the quests contain npcs that are used by this quest, we can not start it
        INPC[] npcs = quest.getNPCs();
        if (npcs != null) {
            for (Quest a : active) {
                for (INPC npc : npcs) {
                    for (INPC n : a.getNPCs()) {
                        if (n.equals(npc)) {
                            return false;
                        }
                    }
                }
            }
        }

        return quest.canStartQuest(player, active, finished);
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
                    for (EventsHandled handled: quest.getHandledEvents()) {
                        eventHandlers.get(handled).add(q);
                    }
                } catch (Exception e) {}
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
