package joshie.harvest.api.npc;

import joshie.harvest.api.npc.task.TaskElement;
import joshie.harvest.api.town.Town;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/** Implemented on my npc entities **/
public interface NPCEntity {
    /** Returns the npc this entity represents **/
    NPC getNPC();

    /** Returns the world object for this npc **/
    World getWorldObj();

    /** Returns a BlockPos representing this entities location **/
    BlockPos getPos();

    /** Returns the Town this npc belongs to **/
    Town getTown();

    /** Set the pathing for the entity
     *  @param tasks   an ordered array of tasks for this npc to complete**/
    void setPath(TaskElement... tasks);
}
