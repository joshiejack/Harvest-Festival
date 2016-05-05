package joshie.harvest.api.npc.ai;

import joshie.harvest.api.npc.INPC;
import net.minecraft.entity.EntityAgeable;

import java.util.UUID;

public interface INPCTask {
    //Return true if this task should terminate
    boolean shouldTerminate(UUID owner, EntityAgeable entity, INPC npc);

    //Return true if this task should start
    boolean shouldExecute(UUID owner, EntityAgeable entity, INPC npc);

    //This task can only be started by a demand not randomly
    boolean demandOnly();

    //Execute the task
    void execute(UUID owner, EntityAgeable entity, INPC npc);
}
