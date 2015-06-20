package joshie.harvest.player.quests;

import static joshie.harvest.core.network.PacketHandler.sendToServer;

import java.util.HashSet;

import joshie.harvest.api.quest.IQuest;
import joshie.harvest.api.shops.IShop;
import joshie.harvest.core.network.quests.PacketQuestCompleted;
import joshie.harvest.core.network.quests.PacketQuestStart;
import joshie.harvest.npc.entity.EntityNPC;
import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class QuestDataClient extends QuestData {
    private HashSet<IQuest> available = new HashSet();
    
    //Adds a quest to the current list
    @Override
    public void addAsCurrent(IQuest quest) {
        current.add(quest);
    }

    @Override
    public void markCompleted(IQuest quest, boolean sendPacket) {
        markCompleted(quest);
        sendToServer(new PacketQuestCompleted(quest, true));
    }
    
    //Removes the quest from the current and available lists
    public void markCompleted(IQuest quest) {
        available.remove(quest);
        current.remove(quest);
    }

    @Override
    public void setAvailable(IQuest quest) {    
        available.add(quest);
    }

    //Called to change the current quests stage
    @Override
    public void setStage(IQuest quest, int stage) {
        IQuest q = getAQuest(quest);
        if (q != null) q.setStage(stage);
    }

    //Returns a single lined script
    @Override
    public String getScript(EntityPlayer player, EntityNPC npc) {      
        if (current != null) {
            for (IQuest q : current) {
                if (q.handlesScript(npc.getNPC())) {
                    String script = q.getScript(player, npc);
                    if (script != null) return script;
                }
            }
        }

        //If we didn't return a current quest, search for a new one
        if (current.size() < 10) {
            for (IQuest q : available) {
                if (!current.contains(q) && q.handlesScript(npc.getNPC())) {
                    try {
                        IQuest quest = ((IQuest) q.getClass().newInstance()).setUniqueName(q.getUniqueName()).setStage(0); //Set the current quest to your new 
                        current.add(quest);
                        sendToServer(new PacketQuestStart(q));
                        String script = quest.getScript(player, npc);
                        if (script != null) return script;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return null;
    }
}
