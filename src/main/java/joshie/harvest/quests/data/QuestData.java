package joshie.harvest.quests.data;

import joshie.harvest.api.npc.INPC;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.npc.entity.EntityNPC;
import net.minecraft.entity.player.EntityPlayer;

import java.util.HashSet;

public abstract class QuestData {
    protected final HashSet<Quest> current = new HashSet<>();

    public HashSet<Quest> getCurrent() {
        return current;
    }

    //Returns a selection maenu
    public Quest getSelection(EntityPlayer player, EntityNPC npc) {
        for (Quest q : current) {
            if (handlesScript(q, npc.getNPC())) {
                if (q.getSelection(player, npc.getNPC()) != null) return q;
            }
        }

        return null;
    }

    boolean handlesScript(Quest quest, INPC npc) {
        INPC[] npcs = quest.getNPCs();
        if (npcs == null) return false;
        else {
            for (INPC n: npcs) {
                if (n.equals(npc)) return true;
            }
        }

        return false;
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