package uk.joshiejack.settlements.inventory;

import uk.joshiejack.settlements.entity.EntityNPC;
import uk.joshiejack.settlements.quest.Quest;
import uk.joshiejack.penguinlib.inventory.ContainerPenguin;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerNPC extends ContainerPenguin {
    private final EntityPlayer player;
    private final EntityNPC npc;
    private final Quest script;

    public ContainerNPC(EntityPlayer player, EntityNPC npc, Quest script) {
        this.player = player;
        this.npc = npc;
        this.script = script;
    }
}
