package joshie.harvest.npc.entity;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class EntityAITalkingTo extends EntityAIBase {
    private AbstractEntityNPC npc;

    public EntityAITalkingTo(AbstractEntityNPC npc) {
        this.npc = npc;
        this.setMutexBits(5);
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
            return entityplayer == null ? false : (npc.getDistanceSqToEntity(entityplayer) > 16.0D ? false : entityplayer.openContainer instanceof Container);
        }
    }

    @Override
    public void startExecuting() {
        npc.getNavigator().clearPathEntity();
    }

    @Override
    public void resetTask() {
        npc.setTalking(null);
    }
}