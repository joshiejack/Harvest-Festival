package joshie.harvest.quests.data;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.CalendarDate;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.api.quests.TargetType;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.util.interfaces.ISyncMaster;
import joshie.harvest.quests.packet.PacketQuestCompleted;
import joshie.harvest.quests.packet.PacketQuestConnect;
import joshie.harvest.quests.packet.PacketQuestRemove;
import joshie.harvest.quests.packet.PacketQuestSetCurrent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static joshie.harvest.core.helpers.SerializeHelper.readMap;
import static joshie.harvest.core.helpers.SerializeHelper.writeMap;

public class QuestDataServer extends QuestData {
    private final ISyncMaster master;
    private Map<Quest, CalendarDate> lastFinished = new HashMap<>();

    public QuestDataServer(ISyncMaster master) {
        this.master = master;
    }

    public void purge(EntityPlayerMP player) {
        current.clear();
        finished.clear();
        lastFinished.clear();
        sync(player); //Resync everything
    }

    public CalendarDate getLastCompletionOfQuest(Quest quest) {
        return lastFinished.get(quest);
    }

    @Override
    public boolean startQuest(Quest q, boolean sync, @Nullable NBTTagCompound tag) {
        try {
            if (!finished.contains(q) || q.isRepeatable()) {
                Quest quest = q.getClass().newInstance().setRegistryName(q.getRegistryName()).setStage(0); //Set the current quest to your new
                if (tag != null) { //Default information
                    quest.readFromNBT(tag);
                }

                current.add(quest);
                if (sync ) {
                    master.sync(null, new PacketQuestSetCurrent(quest));
                }

                return true;
            } else return false;
        } catch (Exception ignored) { return false; }
    }

    private void finish(@Nonnull World world, @Nullable EntityPlayer player, Quest quest, boolean rewards) {
        finished.add(quest);
        if (rewards && player != null) {
            quest.onQuestCompleted(player); //Complete the quest, then add any notes
            quest.getNotes().stream().forEach(note -> HFApi.player.getTrackingForPlayer(player).learnNote(note));
        }

        if (quest.isRepeatable() && quest.getDaysBetween() > 0) {
            lastFinished.put(quest, HFApi.calendar.getDate(world).copy());
        }
    }

    //Quests should always REMOVE from the current quests, and add to the finished quests THEMSELVES
    //Only the person who actually completed the quest, will get the reward
    @Override
    public void markCompleted(@Nonnull World world, @Nullable EntityPlayer player, Quest quest, boolean rewards) {
        Quest localQuest = getAQuest(quest);
        if (localQuest != null) {
            current.remove(localQuest);
            finish(world, player, localQuest, rewards);
        } else finish(world, player, quest, rewards);

        HFTrackers.markTownsDirty();
        //Sync everything
        if ((quest.getQuestType() == TargetType.PLAYER || (quest.getQuestType() == TargetType.TOWN && rewards))) master.sync(player, new PacketQuestCompleted(quest, rewards)); //Let this player claim the reward
        else if (quest.getQuestType() == TargetType.TOWN) master.sync(null, new PacketQuestCompleted(quest, false)); //Let the rest of the server know this was completed
        updateQuests(true); //Update the world on these quests, everytime one is completed
    }

    @Override
    public void removeAsCurrent(@Nonnull World world, Quest quest) {
        current.remove(quest);
        finished.remove(quest);
        HFTrackers.markTownsDirty();
        if (quest.getQuestType() == TargetType.TOWN) master.sync(null, new PacketQuestRemove(quest)); //Let the rest of the server know this was completed
        updateQuests(true); //Update the world on these quests, everytime one is completed
    }
    
    private void updateQuests(boolean sync) {
        for (Quest quest : Quest.REGISTRY.getValues()) {
            updateQuests(quest, sync);
        }
    }

    public void sync(@Nullable EntityPlayerMP player) {
        updateQuests(false); //Don't bother syncing as we're going to send it all at once!
        master.sync(player, new PacketQuestConnect(writeToNBT(new NBTTagCompound())));
    }

    private void updateQuests(Quest quest, boolean sync) {
        if (quest.getQuestType() != master.getTargetType()) return; //If we aren't the same quest type, we don't get counted
        //Check if the quest can be complete
        //If the quest isn't finished, do stuff
        if (!finished.contains(quest) || quest.isRepeatable()) {
            //If we aren't already working on this quest, and we can start it, then start the quest
            if (!current.contains(quest) && canStart(quest, current, finished)) {
                startQuest(quest, sync, null);
            }
        }
    }

    private boolean canStart(Quest quest, Set<Quest> active, Set<Quest> finished) {
        return quest.canStartQuest(active, finished);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        lastFinished = readMap(Quest.class, CalendarDate.class, "LastQuest", nbt);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        writeMap(lastFinished, "LastQuest", nbt);
        return super.writeToNBT(nbt);
    }
}
