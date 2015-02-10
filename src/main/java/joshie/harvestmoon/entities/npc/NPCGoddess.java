package joshie.harvestmoon.entities.npc;

import joshie.harvestmoon.entities.EntityNPC;
import net.minecraft.entity.player.EntityPlayer;

public class NPCGoddess extends NPC {
    public NPCGoddess(String name, Gender gender, Age age) {
        super(name, gender, age);
    }

    @Override
    public boolean respawns() {
        return false;
    }

    @Override
    public void onContainerClosed(EntityPlayer player, EntityNPC npc) {
        npc.setDead();
    }
}
