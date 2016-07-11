package joshie.harvest.npc.entity.ai;

import joshie.harvest.npc.entity.AbstractEntityNPC;

public abstract class AbstractTask {
    //Return true if this task should terminate
    public abstract boolean shouldTerminate(AbstractEntityNPC entity);

    //Return true if this task should start
    public abstract boolean shouldExecute(AbstractEntityNPC entity);

    //Execute the task
    public abstract void execute(AbstractEntityNPC entity);
}
