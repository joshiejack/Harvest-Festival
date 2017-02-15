package joshie.harvest.api.npc.schedule;

import net.minecraft.entity.EntityAgeable;

public abstract class ScheduleElement<E> {
    private boolean satisfied = false;

    public boolean isSatisfied(EntityAgeable npc) {
        return satisfied;
    }

    public void execute(EntityAgeable npc) {
        satisfied = true;
    }
}
