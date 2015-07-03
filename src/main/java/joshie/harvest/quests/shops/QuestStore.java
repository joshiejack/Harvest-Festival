package joshie.harvest.quests.shops;

import joshie.harvest.api.npc.INPC;
import joshie.harvest.quests.Quest;
import net.minecraft.entity.player.EntityPlayer;

public class QuestStore extends Quest {
    private INPC[] npc;

    public QuestStore(INPC npc) {
        this.npc = new INPC[] { npc };
    }

    @Override
    protected INPC[] getNPCs() {
        return npc;
    }
    
    @Override
    public int getOptions() {
        return 3;
    }
    
    @Override
    public void onSelected(EntityPlayer player, int option) {
        
    }
}
