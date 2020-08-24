package joshie.harvest.npcs.entity.ai;

import joshie.harvest.api.npc.task.TaskElement;
import joshie.harvest.api.npc.task.TaskList;
import joshie.harvest.npcs.entity.EntityNPCHuman;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.nbt.NBTTagCompound;

public class EntityAIPathing extends EntityAIBase {
    private final EntityNPCHuman npc;
    private TaskList path;
    private TaskElement target;
    private int scheduleTimer;

    public EntityAIPathing(EntityNPCHuman npc) {
        this.npc = npc;
        this.setMutexBits(1);
    }

    public void setPath(TaskElement... elements) {
        path = TaskList.target(elements);
        recalculateTarget();
    }

    public TaskList getPath() {
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
    public boolean shouldContinueExecuting() {
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

    public void readFromNBT(NBTTagCompound tag) {
        if (tag.hasKey("Path")) {
            path = TaskList.fromNBT(tag.getCompoundTag("Path"));
        }
    }

    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        if (path != null) {
            tag.setTag("Path", path.toNBT(new NBTTagCompound()));
        }

        return tag;
    }
}
