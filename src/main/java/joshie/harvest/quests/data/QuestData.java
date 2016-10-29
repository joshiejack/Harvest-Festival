package joshie.harvest.quests.data;

import joshie.harvest.api.quests.Quest;
import joshie.harvest.npc.entity.EntityNPC;
import net.minecraft.entity.player.EntityPlayer;

import java.util.HashSet;

public abstract class QuestData {
    protected final HashSet<Quest> current = new HashSet<>();

    public HashSet<Quest> getCurrent() {
        return current;
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

    public Quest getAQuest(Quest quest) {
        //Create the quest if it doesn't exist
        if (!quest.isRealQuest() && !current.contains(quest)) {
            startQuest(quest);
        }

        //Search the quests for the real version
        for (Quest q : current) {
            if (q.equals(quest)) {
                return q;
            }
        }

        return null;

    }

    public abstract void markCompleted(EntityPlayer player, Quest quest, boolean rewards);
    public boolean startQuest(Quest quest) { return false; }
}