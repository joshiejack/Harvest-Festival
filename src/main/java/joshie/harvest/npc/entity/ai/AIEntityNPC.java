package joshie.harvest.npc.entity.ai;

import joshie.harvest.npc.entity.AbstractEntityNPC;
import net.minecraft.entity.ai.EntityAIBase;

import java.util.ArrayList;

public class AIEntityNPC extends EntityAIBase {
    private static final ArrayList<AbstractTask> tasks = new ArrayList<>();
    private AbstractEntityNPC npc;

    public AIEntityNPC(AbstractEntityNPC npc) {
        this.npc = npc;
    }

    static {
        tasks.add(new TaskGoHome());
    }

    @Override
    public boolean shouldExecute() {
        if (npc.getTask() != null) return true;
        else {
            for (AbstractTask task: tasks) {
                if (task.shouldExecute(npc)) {
                    npc.setTask(task);
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean continueExecuting() {
        if (npc.getTask() != null) {
            if (npc.getTask().shouldTerminate(npc)) {
                npc.setTask(null);
            }
        }

        return false;
    }

    @Override
    public void startExecuting() {
        npc.getTask().execute(npc);
    }
}
