package joshie.harvest.npc.entity.ai;

import joshie.harvest.npc.entity.EntityNPC;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;

public class EntityAILookAtPlayer extends EntityAIWatchClosest {
    private final EntityNPC npc;

    public EntityAILookAtPlayer(EntityNPC npc) {
        super(npc, EntityPlayer.class, 8.0F);
        this.npc = npc;
        this.setMutexBits(5);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute() {
        return npc != null && npc.isTalking() && npc.getTalkingTo() != null;
    }
}