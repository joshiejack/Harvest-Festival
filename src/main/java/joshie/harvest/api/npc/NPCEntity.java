package joshie.harvest.api.npc;

import joshie.harvest.api.npc.task.TaskElement;
import joshie.harvest.api.town.Town;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.util.math.BlockPos;

/** Implemented on my npc entities **/
public interface NPCEntity {
    /** Returns the npc this entity represents **/
    NPC getNPC();

    /** Returns this as an entity **/
    EntityAgeable getAsEntity();

    /** Returns a BlockPos representing this entities location **/
    BlockPos getPos();

    /** Returns the Town this npc belongs to **/
    Town getTown();

    /** Set the pathing for the entity
     *  @param tasks   an ordered array of tasks for this npc to complete**/
    void setPath(TaskElement... tasks);
}