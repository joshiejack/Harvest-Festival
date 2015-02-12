package joshie.harvestmoon.player;

import static joshie.harvestmoon.network.PacketHandler.sendToServer;

import java.util.HashSet;

import joshie.harvestmoon.network.quests.PacketQuestStart;
import joshie.harvestmoon.npc.EntityNPC;
import joshie.harvestmoon.quests.Quest;
import joshie.harvestmoon.shops.ShopInventory;
import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class QuestsClientside {
    private HashSet<Quest> available = new HashSet();
    private HashSet<Quest> current = new HashSet(10);

    public HashSet<Quest> getCurrent() {
        return current;
    }

    //Returns the quest in the current list that is the instance of this quest type
    private Quest getAQuest(Quest quest) {
        if (current != null) {
            for (Quest q : current) {
                if (q.equals(quest)) {
                    return q;
                }
            }
        }

        return null;

    }

    //Adds a quest to the current list
    public void addAsCurrent(Quest quest) {
        current.add(quest);
    }
    
    //Removes the quest from the current and available lists
    public void markCompleted(Quest quest) {
        available.remove(quest);
        current.remove(quest);
    }

    //Adds a quest to the available list
    public void setAvailable(Quest quest) {    
        available.add(quest);
    }

    //Called to change the current quests stage
    public void setStage(Quest quest, int stage) {
        Quest q = getAQuest(quest);
        if (q != null) q.setStage(stage);
    }

    //Returns a single lined script
    public String getScript(EntityPlayer player, EntityNPC npc) {      
        ShopInventory shop = npc.getNPC().getShop();
        if(shop != null && shop.isOpen(player.worldObj)) {
            return shop.getWelcome();
        }
        
        if (current != null) {
            for (Quest q : current) {
                if (q.handlesScript(npc.getNPC())) {
                    String script = q.getScript(player, npc);
                    if (script != null) return script;
                }
            }
        }

        //If we didn't return a current quest, search for a new one
        if (current.size() < 10) {
            for (Quest q : available) {
                if (!current.contains(q) && q.handlesScript(npc.getNPC())) {
                    try {
                        Quest quest = ((Quest) q.getClass().newInstance()).setName(q.getName()).setStage(0); //Set the current quest to your new 
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

        return npc.getNPC().getScript();
    }
}
