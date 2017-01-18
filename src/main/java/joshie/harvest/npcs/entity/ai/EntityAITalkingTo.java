package joshie.harvest.npcs.entity.ai;

import joshie.harvest.npcs.entity.EntityNPC;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;

public class EntityAITalkingTo extends EntityAIBase {
    private final EntityNPC npc;

    public EntityAITalkingTo(EntityNPC npc) {
        this.npc = npc;
        this.setMutexBits(1);
    }

    @Override
    public boolean shouldExecute() {
        if (!npc.isEntityAlive()) {
            return false;
        } else if (npc.isInWater()) {
            return false;
        } else if (!npc.onGround) {
            return false;
        } else if (npc.velocityChanged) {
            return false;
        } else {
            EntityPlayer entityplayer = npc.getTalkingTo();
            return entityplayer != null && npc.getDistanceSqToEntity(entityplayer) > 16.0D && entityplayer.openContainer != null;
        }
    }

    @Override
    public void startExecuting() {
        npc.getNavigator().clearPathEntity();
    }
}