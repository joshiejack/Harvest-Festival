package joshie.harvest.npc.entity.ai;

import joshie.harvest.npc.entity.EntityNPC;

public abstract class AbstractTask {
    //Return true if this task should terminate
    public abstract boolean shouldTerminate(EntityNPC entity);

    //Return true if this task should start
    public abstract boolean shouldExecute(EntityNPC entity);

    //Execute the task
    public abstract void execute(EntityNPC entity);
}
