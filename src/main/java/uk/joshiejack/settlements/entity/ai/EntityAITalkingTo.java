package uk.joshiejack.settlements.entity.ai;

import uk.joshiejack.settlements.entity.EntityNPC;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;

public class EntityAITalkingTo extends EntityAIBase {
    private final EntityNPC npc;

    public EntityAITalkingTo(EntityNPC npc) {
        this.npc = npc;
        this.setMutexBits(2);
    }

    @Override
    public boolean shouldExecute() {
        for (EntityPlayer player: npc.getTalkingTo()) {
            if (npc.getDistanceSq(player) < 3D) return true;
        }

        return false;
    }

    @Override
    public void startExecuting() {
        npc.getNavigator().clearPath();
    }
}
