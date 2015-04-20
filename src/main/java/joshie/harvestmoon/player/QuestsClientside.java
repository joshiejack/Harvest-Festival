package joshie.harvestmoon.player;

import static joshie.harvestmoon.core.network.PacketHandler.sendToServer;

import java.util.HashSet;

import joshie.harvestmoon.api.quest.IQuest;
import joshie.harvestmoon.api.shops.IShop;
import joshie.harvestmoon.core.network.quests.PacketQuestStart;
import joshie.harvestmoon.npc.EntityNPC;
import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class QuestsClientside {
    private HashSet<IQuest> available = new HashSet();
    private HashSet<IQuest> current = new HashSet(10);

    public HashSet<IQuest> getCurrent() {
        return current;
    }

    //Returns the quest in the current list that is the instance of this quest type
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

    //Adds a quest to the current list
    public void addAsCurrent(IQuest quest) {
        current.add(quest);
    }
    
    //Removes the quest from the current and available lists
    public void markCompleted(IQuest quest) {
        available.remove(quest);
        current.remove(quest);
    }

    //Adds a quest to the available list
    public void setAvailable(IQuest quest) {    
        available.add(quest);
    }

    //Called to change the current quests stage
    public void setStage(IQuest quest, int stage) {
        IQuest q = getAQuest(quest);
        if (q != null) q.setStage(stage);
    }

    //Returns a single lined script
    public String getScript(EntityPlayer player, EntityNPC npc) {      
        IShop shop = npc.getNPC().getShop();
        if(shop != null && shop.isOpen(player.worldObj)) {
            return shop.getWelcome();
        }
        
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

        return npc.getNPC().getGreeting();
    }
}
