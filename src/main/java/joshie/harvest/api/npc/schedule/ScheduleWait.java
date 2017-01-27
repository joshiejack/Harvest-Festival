package joshie.harvest.api.npc.schedule;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.util.math.BlockPos;

public class ScheduleWait extends ScheduleElement<BlockPos> {
    private final int target;
    private int ticker;

    private ScheduleWait(int target) {
        this.target = target;
    }

    public static ScheduleWait of(int target) {
        return new ScheduleWait(target);
    }

    @Override
    public void execute(EntityAgeable npc) {
        npc.getNavigator().clearPathEntity();
        ticker++; //Continue Executing
    }

    @Override
    public boolean isSatisfied(EntityAgeable npc) {
        return ticker >= target;
    }
}
