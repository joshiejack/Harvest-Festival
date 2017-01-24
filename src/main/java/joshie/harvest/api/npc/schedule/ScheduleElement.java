package joshie.harvest.api.npc.schedule;

import net.minecraft.entity.EntityAgeable;

public abstract class ScheduleElement<E> {
    public boolean isSatisfied(EntityAgeable npc) {
        return true;
    }

    public void execute(EntityAgeable npc) {}
}
