package joshie.harvest.api.npc.ai;

import joshie.harvest.api.npc.INPC;
import joshie.harvest.npc.entity.EntityNPC;

public interface INPCTask {
    //Return true if this task should terminate
    boolean shouldTerminate(EntityNPC entity, INPC npc);

    //Return true if this task should start
    boolean shouldExecute(EntityNPC entity, INPC npc);

    //This task can only be started by a demand not randomly
    boolean demandOnly();

    //Execute the task
    void execute(EntityNPC entity, INPC npc);
}
