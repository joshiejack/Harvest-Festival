package joshie.harvest.quests.data;

import joshie.harvest.api.quests.Quest;
import joshie.harvest.npc.entity.EntityNPC;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class QuestDataClient extends QuestData {
    public void addAsCurrent(Quest quest) {
        current.add(quest);
    }
    
    //Removes the quest from the current and available lists
    @Override
    public void markCompleted(EntityPlayer player, Quest quest, boolean rewards) {
        Quest aQuest = getAQuest(quest);
        if (aQuest != null && rewards) {
            aQuest.onQuestCompleted(player);
        }

        current.remove(quest);
        finished.add(quest);
    }

    private String getScript(Quest quest, EntityPlayer player, EntityNPC entity) {
        return quest.getLocalizedScript(player, entity, entity.getNPC());
    }

    public String getScript(EntityPlayer player, EntityNPC npc) {
        for (Quest q : current) {
            if (q.getNPCs().contains(npc.getNPC())) {
                String script = getScript(q, player, npc);
                if (script != null) return script;
            }
        }

        return null;
    }
}
