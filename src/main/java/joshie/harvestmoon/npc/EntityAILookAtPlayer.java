package joshie.harvestmoon.npc;

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
        if (npc.isTalking()) {
            closestEntity = npc.getTalkingTo();
            return true;
        } else {
            return false;
        }
    }
}