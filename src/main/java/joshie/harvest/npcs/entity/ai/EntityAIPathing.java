package joshie.harvest.npcs.entity.ai;

import joshie.harvest.api.npc.schedule.ScheduleElement;
import joshie.harvest.api.npc.schedule.SchedulePath;
import joshie.harvest.npcs.entity.EntityNPCHuman;
import net.minecraft.entity.ai.EntityAIBase;

public class EntityAIPathing extends EntityAIBase {
    private final EntityNPCHuman npc;
    private SchedulePath path;
    private ScheduleElement target;
    private int scheduleTimer;

    public EntityAIPathing(EntityNPCHuman npc) {
        this.npc = npc;
        this.setMutexBits(1);
    }

    public void setPath(ScheduleElement... elements) {
        path = SchedulePath.target(elements);
        recalculateTarget();
    }

    public SchedulePath getPath() {
        return this.path;
    }

    public void recalculateTarget() {
        target = getPath().getCurrentTarget(npc);
    }

    @Override
    public boolean shouldExecute() {
        return getPath() != null;
    }

    @Override
    public boolean continueExecuting() {
        return getPath() != null && target != null;
    }

    @Override
    public void updateTask() {
        scheduleTimer++;
        if (scheduleTimer %10 == 0) recalculateTarget();
        if (target != null && scheduleTimer % 60 == 0) {
            target.execute(npc);
        }

        if (target == null) path = null; //Clear this up
    }
}
