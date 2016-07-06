package joshie.harvest.npc.entity.ai;

import joshie.harvest.npc.entity.AbstractEntityNPC;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;

public class EntityAILookAtPlayer extends EntityAIWatchClosest {
    private final AbstractEntityNPC npc;

    public EntityAILookAtPlayer(AbstractEntityNPC npc) {
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