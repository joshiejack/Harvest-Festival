package joshie.harvest.player.quests;

import java.util.HashSet;

import joshie.harvest.api.quest.IQuest;
import joshie.harvest.npc.entity.EntityNPC;
import net.minecraft.entity.player.EntityPlayer;

public abstract class QuestData {
    protected HashSet<IQuest> current = new HashSet(10);

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

    public String getScript(EntityPlayer player, EntityNPC npc) {
        return "";
    }

    public abstract void markCompleted(IQuest quest, boolean sendPacket);
    public void setAvailable(IQuest quest) {}
    public void addAsCurrent(IQuest quest) {}
    public abstract void setStage(IQuest theQuest, int stage);
    public boolean startQuest(IQuest quest) { return false; }
}
