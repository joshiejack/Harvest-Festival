package uk.joshiejack.settlements.entity.ai;

import uk.joshiejack.settlements.entity.EntityNPC;
import uk.joshiejack.penguinlib.util.helpers.minecraft.TimeHelper;
import net.minecraft.entity.ai.EntityAIBase;

public class EntityAISchedule extends EntityAIBase {
    private int cooldown = 0;
    private final EntityNPC npc;

    public EntityAISchedule(EntityNPC npc) {
        this.npc = npc;
        this.setMutexBits(1);
    }

    @Override
    public boolean shouldExecute() {
        if (cooldown > 0) {
            cooldown--;
        }

        return cooldown == 0 && TimeHelper.getTimeOfDay(npc.world.getWorldTime() % 250) == 0;
    }

    @Override
    public void updateTask() {
        cooldown = 25;
        npc.getInfo().callScript("onNPCScheduleUpdate", npc, TimeHelper.getTimeOfDay(npc.world.getWorldTime()));
    }

    @Override
    public void startExecuting() {
        npc.getNavigator().clearPath();
    }
}
