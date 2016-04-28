package joshie.harvest.api.npc;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class NPCEvents extends PlayerEvent {
    public final INPC npc;
    
    public NPCEvents(EntityPlayer player, INPC npc) {
        super(player);
        this.npc = npc;
    }
    
    public static class NPCChatEvent extends NPCEvents {
        public NPCChatEvent(EntityPlayer player, INPC npc) {
            super(player, npc);
        }
    }

}