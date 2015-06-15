package joshie.harvest.player;

import static joshie.harvest.core.helpers.ServerHelper.markDirty;
import static joshie.harvest.core.network.PacketHandler.sendToClient;

import java.util.HashSet;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.quest.IQuest;
import joshie.harvest.core.network.quests.PacketQuestSetAvailable;
import joshie.harvest.core.network.quests.PacketQuestSetCurrent;
import joshie.harvest.core.util.IData;
import joshie.harvest.quests.QuestRegistry;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

public class QuestStats implements IData {
    private HashSet<IQuest> finished = new HashSet();
    private HashSet<IQuest> current = new HashSet(10);

    public PlayerTrackerServer master;

    public QuestStats(PlayerTrackerServer master) {
        this.master = master;
    }

    public HashSet<IQuest> getCurrent() {
        return current;
    }

    public IQuest getAQuest(IQuest quest) {
        if (current != null) {
            for (IQuest q : current) {
                if (q.equals(quest)) {
                    return q;
                }
            }
        }

        return null;
    }

    //Called to start a quest, is called clientside, by the startquest packet
    public boolean startQuest(IQuest q) {
        if (current.size() < 10) {
            try {
                IQuest quest = ((IQuest) q.getClass().newInstance()).setUniqueName(q.getUniqueName()).setStage(0); //Set the current quest to your new 
                current.add(quest);
                syncQuest(q);
            } catch (Exception e) {}

            return true;
        } else return false;
    }

    public void setStage(IQuest quest, int stage) {
        IQuest q = getAQuest(quest);
        if (q != null) {
            q.setStage(stage);
        }

        markDirty();
    }

    //Quests should always REMOVE from the current quests, and add to the finished quests THEMSELVES
    public void markCompleted(IQuest quest) {
        IQuest q = getAQuest(quest);
        if (q != null) {
            q.claim(master.getAndCreatePlayer());
            finished.add(q);
            current.remove(q);
            syncQuests();
        }
    }

    public void syncQuests() {
        for (IQuest quest : QuestRegistry.getQuests().values()) {
            syncQuest(quest);
        }
    }

    public void syncQuest(IQuest quest) {
        //Check if the quest can be complete
        //If the quest isn't finished, do stuff
        if (!finished.contains(quest)) {
            //If the quest is in the currently active list, mark it as current
            if (current.contains(quest)) {
                //Send a packet, fetching the actual quest details that are saved, so we're update to date on the info
                sendToClient(new PacketQuestSetCurrent(getAQuest(quest)), master.getAndCreatePlayer());
            } else {
                //Now the quests aren't in the current list has been determined, let's determine whether this quest is valid for being collected
                //If the quest can be started, we should send it to client to be added to the available list
                if (quest.canStart(master.getAndCreatePlayer(), current, finished)) {
                    sendToClient(new PacketQuestSetAvailable(quest), master.getAndCreatePlayer());
                }
            }
        }

        markDirty();
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        if (nbt.hasKey("CurrentQuests")) {
            NBTTagList list = nbt.getTagList("CurrentQuests", 10);
            for (int i = 0; i < list.tagCount(); i++) {
                NBTTagCompound tag = list.getCompoundTagAt(i);
                IQuest q = HFApi.QUESTS.get(tag.getString("QuestID"));
                try {
                    IQuest quest = ((IQuest) q.getClass().newInstance()).setUniqueName(q.getUniqueName());
                    quest.readFromNBT(tag);
                    current.add(quest);
                } catch (Exception e) {}
            }
        }

        if (nbt.hasKey("FinishedQuests")) {
            NBTTagList list = nbt.getTagList("FinishedQuests", 8);
            for (int i = 0; i < list.tagCount(); i++) {
                finished.add(HFApi.QUESTS.get(list.getStringTagAt(i)));
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        NBTTagList quests = new NBTTagList();
        for (IQuest s : current) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("QuestID", s.getUniqueName());
            s.writeToNBT(tag);
            quests.appendTag(tag);
        }

        nbt.setTag("CurrentQuests", quests);

        NBTTagList done = new NBTTagList();
        for (IQuest s : finished) {
            if (s != null) {
                done.appendTag(new NBTTagString(s.getUniqueName()));
            }
        }

        nbt.setTag("FinishedQuests", done);
    }
}
