package uk.joshiejack.settlements.inventory;

import uk.joshiejack.settlements.entity.EntityNPC;
import uk.joshiejack.settlements.quest.Quest;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerNPCChat extends ContainerNPC {
    public ContainerNPCChat(EntityPlayer player, EntityNPC npc, Quest script) {
        super(player, npc, script);
    }
}
