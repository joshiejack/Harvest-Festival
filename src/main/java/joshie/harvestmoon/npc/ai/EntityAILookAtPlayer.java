package joshie.harvestmoon.npc.ai;

import joshie.harvestmoon.npc.EntityNPC;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;

public class EntityAILookAtPlayer extends EntityAIWatchClosest {
    private final EntityNPC npc;

    public EntityAILookAtPlayer(EntityNPC npc) {
        super(npc, EntityPlayer.class, 8.0F);
        this.npc = npc;
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute() {
        if (npc != null && npc.isTalking()) {
            closestEntity = npc.getTalkingTo();
            if (closestEntity == null) {
                return false;
            } else return true;
        } else {
            return false;
        }
    }
}