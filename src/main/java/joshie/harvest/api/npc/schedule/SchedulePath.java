package joshie.harvest.api.npc.schedule;

import net.minecraft.entity.EntityAgeable;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;

public class SchedulePath {
    private final Queue<ScheduleElement> targets = new LinkedList<>();
    private SchedulePath() {}

    public static SchedulePath target(ScheduleElement... pos) {
        SchedulePath path = new SchedulePath();
        Collections.addAll(path.targets, pos);
        return path;
    }

    public ScheduleElement getCurrentTarget(EntityAgeable npc) {
        ScheduleElement target = targets.peek();
        if (target == null || target.isSatisfied(npc)) {
            return targets.poll();
        }

        return target;
    }
}
