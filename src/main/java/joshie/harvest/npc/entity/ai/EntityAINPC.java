package joshie.harvest.npc.entity.ai;

import joshie.harvest.api.npc.ai.INPCTask;
import joshie.harvest.npc.entity.EntityNPC;
import net.minecraft.entity.ai.EntityAIBase;

import java.util.ArrayList;

public class EntityAINPC extends EntityAIBase {
    private static final ArrayList<INPCTask> tasks = new ArrayList<>();
    private EntityNPC npc;

    public EntityAINPC(EntityNPC npc) {
        this.npc = npc;
    }

    static {
        tasks.add(new TaskGoHome());
    }

    @Override
    public boolean shouldExecute() {
        if (npc.getTask() != null) return true;
        else {
            for (INPCTask task: tasks) {
                if (task.shouldExecute(npc, npc.getNPC())) {
                    System.out.println("Going home");
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
            if (npc.getTask().shouldTerminate(npc, npc.getNPC())) {
                System.out.println("Terminating task");
                npc.setTask(null);
            }
        }

        return false;
    }

    @Override
    public void startExecuting() {
        npc.getTask().execute(npc, npc.getNPC());
    }
}
