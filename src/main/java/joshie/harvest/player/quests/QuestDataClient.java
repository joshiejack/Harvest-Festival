package joshie.harvest.player.quests;

import joshie.harvest.api.quests.Quest;
import joshie.harvest.core.helpers.MCClientHelper;
import joshie.harvest.npc.entity.EntityNPC;
import joshie.harvest.quests.packet.PacketQuestStart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashSet;

import static joshie.harvest.core.network.PacketHandler.sendToServer;

@SideOnly(Side.CLIENT)
public class QuestDataClient extends QuestData {
    private final HashSet<Quest> available = new HashSet<>();
    
    //Adds a quest to the current list
    @Override
    public void addAsCurrent(Quest quest) {
        current.add(quest);
    }
    
    //Removes the quest from the current and available lists
    public void markCompleted(Quest quest) {
        getAQuest(quest).onQuestCompleted(MCClientHelper.getPlayer());
        if (!quest.isRepeatable()) {
            available.remove(quest);
        }

        current.remove(quest);
    }

    @Override
    public void setAvailable(Quest quest) {
        available.add(quest);
    }

    private String getScript(Quest quest, EntityPlayer player, EntityNPC entity) {
        return quest.getLocalizedScript(player, entity, entity.getNPC());
    }

    @Override
    public String getScript(EntityPlayer player, EntityNPC npc) {
        if (current != null) {
            for (Quest q : current) {
                if (handlesScript(q, npc.getNPC())) {
                    String script = getScript(q, player, npc);
                    if (script != null) return script;
                }
            }

            //If we didn't return a current quest, search for a new one
            if (current.size() < 100) {
                for (Quest q : available) {
                    if (!current.contains(q) && handlesScript(q, npc.getNPC())) {
                        try {
                            Quest quest = q.getClass().newInstance().setRegistryName(q.getRegistryName()).setStage(0); //Set the current quest to your new
                            current.add(quest);
                            sendToServer(new PacketQuestStart(q));
                            String script = getScript(q, player, npc);
                            if (script != null && handlesScript(quest, npc.getNPC())) return script;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        return null;
    }
}
