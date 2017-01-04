package joshie.harvest.quests.data;

import joshie.harvest.api.quests.Quest;
import joshie.harvest.npc.entity.EntityNPC;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

public abstract class QuestData {
    protected final Set<Quest> current = new HashSet<>();
    protected final Set<Quest> finished = new HashSet<>();

    public Set<Quest> getCurrent() {
        return current;
    }

    public Set<Quest> getFinished() {
        return finished;
    }

    //Returns a selection menu
    public Quest getSelection(EntityPlayer player, EntityNPC npc) {
        for (Quest q : current) {
            if (q.getNPCs().contains(npc.getNPC())) {
                if (q.getSelection(player, npc.getNPC()) != null) return q;
            }
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    public <Q extends Quest> Q getAQuest(Quest quest) {
        //Create the quest if it doesn't exist
        if (!quest.isRealQuest() && !current.contains(quest)) {
            startQuest(quest, false, null);
        }

        //Search the quests for the real version
        for (Quest q : current) {
            if (q.equals(quest)) {
                return (Q) q;
            }
        }

        return null;

    }

    public abstract void markCompleted(EntityPlayer player, Quest quest, boolean rewards);
    public boolean startQuest(Quest quest, boolean sync, @Nullable NBTTagCompound tag) { return false; }

    public void readFromNBT(NBTTagCompound nbt) {
        if (nbt.hasKey("CurrentQuests")) {
            NBTTagList list = nbt.getTagList("CurrentQuests", 10);
            for (int i = 0; i < list.tagCount(); i++) {
                NBTTagCompound tag = list.getCompoundTagAt(i);
                Quest q = Quest.REGISTRY.getValue(new ResourceLocation(tag.getString("QuestID")));
                if (q != null) {
                    try {
                        Quest quest = q.getClass().newInstance().setRegistryName(q.getRegistryName());
                        quest.readFromNBT(tag);
                        current.add(quest);
                    } catch (InstantiationException | IllegalAccessException e) { /**/}
                }
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