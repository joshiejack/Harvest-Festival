package joshie.harvestmoon.quests;

import static joshie.harvestmoon.helpers.QuestHelper.completeQuest;
import static joshie.harvestmoon.helpers.QuestHelper.rewardGold;
import static joshie.harvestmoon.helpers.QuestHelper.rewardRelations;
import static joshie.harvestmoon.helpers.QuestHelper.takeHeldStack;

import java.util.HashSet;

import joshie.harvestmoon.entities.EntityNPC;
import joshie.harvestmoon.entities.NPC;
import joshie.harvestmoon.helpers.CropHelper;
import joshie.harvestmoon.init.HMCrops;
import joshie.harvestmoon.init.HMItems;
import joshie.harvestmoon.init.HMNPCs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;

public class QuestTomatoes extends Quest {    
    @Override
    public boolean canStart(EntityPlayer player, HashSet<Quest> active, HashSet<Quest> finished) {
        if(!super.canStart(player, active, finished)) return false;
        else {
            return true;
        }
    }
    
    @Override
    public NPC[] getNPCs() {
        return new NPC[] { HMNPCs.goddess };
    }
    
    @Override
    public String getScript(EntityPlayer player, EntityNPC npc) {     
        if(quest_stage == 0) {            
            increaseStage(player);
            return getLocalized("start");
        } else if(player.getCurrentEquippedItem() != null) {              
            ItemStack held = player.getCurrentEquippedItem();
            if(held.stackSize >= 10) {
                if(held.getItem() == HMItems.crops && CropHelper.getCropFromStack(held) == HMCrops.tomato) {
                    takeHeldStack(player, 10);
                    completeQuest(player, this);
                    return getLocalized("finish");
                }
            }
            
            return getLocalized("wrong");
        }
        
        return null;
    }
    
    @Override
    public void claim(EntityPlayerMP player) {
        rewardGold(player, HMCrops.tomato.getSellValue() * 15);
        rewardRelations(player, HMNPCs.goddess, 1000);
    }
}
